package com.gigster.skymarket.service.impl;

import com.gigster.skymarket.dto.CatalogueDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.exception.CatalogueAlreadyExistsException;
import com.gigster.skymarket.exception.ResourceNotFoundException;
import com.gigster.skymarket.mapper.CatalogueMapper;
import com.gigster.skymarket.model.*;
import com.gigster.skymarket.repository.CatalogueRepository;
import com.gigster.skymarket.repository.CategoryRepository;
import com.gigster.skymarket.repository.CustomerRepository;
import com.gigster.skymarket.repository.ProductRepository;
import com.gigster.skymarket.service.CatalogueService;
import com.gigster.skymarket.service.NotificationService;
import com.gigster.skymarket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CatalogueServiceImpl implements CatalogueService {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerRepository customerRepository;


    private final CatalogueRepository catalogueRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CatalogueMapper catalogueMapper;

    public CatalogueServiceImpl(CatalogueRepository catalogueRepository,
                                CategoryRepository categoryRepository,
                                ProductRepository productRepository,
                                CatalogueMapper catalogueMapper) {
        this.catalogueRepository = catalogueRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.catalogueMapper = catalogueMapper;
    }

    @Override
    public Catalogue createCatalogue(Catalogue catalogue) {
        // 1. Get the currently authenticated user
        User currentUser = userService.getCurrentAuthenticatedUser();

        // 2. Check if the user has permission to create a catalogue (optional)
        if (currentUser.hasRole("ROLE_SUPER_ADMIN") && currentUser.hasRole("ROLE_ADMIN")) {
            throw new AccessDeniedException("You do not have permission to create a catalogue.");
        }

        // 3. Associate the catalogue with the current user (if applicable)
        catalogue.setCreatedBy(currentUser);
        catalogue.setCreatedOn(String.valueOf(LocalDateTime.now()));

        if (catalogue.getName() == null || catalogue.getName().isEmpty()) {
            throw new IllegalArgumentException("Catalogue name cannot be null or empty.");
        }

        // 5. Check for duplicates
        if (catalogueRepository.existsByName(catalogue.getName())) {
            throw new CatalogueAlreadyExistsException("Catalogue with this name already exists.");
        }

        // 6. Initialize categories if null
        if (catalogue.getCategories() == null) {
            catalogue.setCategories(new HashSet<>());
        }

        // 7. Save the catalogue
        return catalogueRepository.save(catalogue);
    }

    @Override
    public Catalogue getCatalogueById(Long id) {
        return catalogueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catalogue", "id", id));
    }

    @Override
    public ResponseEntity<ResponseDto> getAllCatalogues(int page, int size, String sort) {
        String[] sortParams = sort.split(",");
        Sort.Direction direction = Sort.Direction.ASC;

        if (sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParams[0]));
        Page<Catalogue> cataloguesPage = catalogueRepository.findAll(pageable);

        List<CatalogueDto> catalogueDtos = cataloguesPage.getContent()
                .stream()
                .map(catalogueMapper::toDto)
                .collect(Collectors.toList());

        ResponseDto responseDto = ResponseDto.builder()
                .status(HttpStatus.OK)
                .description("List of All Catalogues.")
                .payload(catalogueDtos)
                .totalPages(cataloguesPage.getTotalPages())
                .totalElements(cataloguesPage.getTotalElements())
                .currentPage(cataloguesPage.getNumber())
                .pageSize(cataloguesPage.getSize())
                .build();

        return ResponseEntity.ok(responseDto);
    }


    @Override
    public Catalogue updateCatalogue(Long id, Catalogue updatedCatalogue) {
        Catalogue existingCatalogue = getCatalogueById(id);
        existingCatalogue.setName(updatedCatalogue.getName());
        existingCatalogue.setDescription(updatedCatalogue.getDescription());
        return catalogueRepository.save(existingCatalogue);
    }

    @Override
    public void deleteCatalogue(Long id) {
        Catalogue catalogue = getCatalogueById(id);
        catalogueRepository.delete(catalogue);
    }

    @Override
    public Catalogue assignCategoriesToCatalogue(Long catalogueId, List<Long> categoryIds) {
        Catalogue catalogue = catalogueRepository.findById(catalogueId)
                .orElseThrow(() -> new RuntimeException("Catalogue not found"));
        List<Category> categories = categoryRepository.findAllById(categoryIds);
        Set<Category> categorySet = new HashSet<>(categories);
        catalogue.setCategories(categorySet);
        return catalogueRepository.save(catalogue);
    }

    @Override
    public Catalogue addCategoryToCatalogue(Long catalogueId, Long categoryId) {
        Catalogue catalogue = getCatalogueById(catalogueId);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        catalogue.getCategories().add(category);
        return catalogueRepository.save(catalogue);
    }

    @Override
    public Catalogue removeCategoryFromCatalogue(Long catalogueId, Long categoryId) {
        Catalogue catalogue = getCatalogueById(catalogueId);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        catalogue.getCategories().remove(category);
        return catalogueRepository.save(catalogue);
    }

    @Override
    public List<Category> getCategoriesInCatalogue(Long catalogueId) {
        Catalogue catalogue = getCatalogueById(catalogueId);
        return new ArrayList<>(catalogue.getCategories());
    }

    @Override
    public ResponseDto getProductsInCatalogue(Long catalogueId, Pageable pageable) {
        // Fetch the catalogue by ID
        Catalogue catalogue = getCatalogueById(catalogueId);

        // Initialize variables to store products and pagination details
        List<Product> allProducts = new ArrayList<>();
        int totalElements = 0;
        int totalPages = 0;

        // Loop through categories and fetch paginated products
        for (Category category : catalogue.getCategories()) {
            // Fetch paginated products for each category
            Page<Product> productPage = productRepository.findByCategoryId(category.getCategoryId(), pageable);

            // Add the products from this page to the allProducts list
            allProducts.addAll(productPage.getContent());

            // Track the total number of elements and total pages
            totalElements += (int) productPage.getTotalElements();
            totalPages = productPage.getTotalPages();
        }

        return ResponseDto.builder()
                .status(HttpStatus.OK)
                .description("List of Products in Catalogue.")
                .payload(allProducts)
                .totalElements((long) totalElements)
                .totalPages(totalPages)
                .currentPage(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .build();
    }

    @Override
    public Catalogue addProductsToCatalogue(Long catalogueId, List<Long> productIds) {
        Catalogue catalogue = getCatalogueById(catalogueId);
        for (Long productId : productIds) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
            catalogue.getProducts().add(product);
        }
        return catalogueRepository.save(catalogue);
    }

    @Override
    public List<Catalogue> filterCataloguesByCategory(String categoryName) {
        List<Category> categories = categoryRepository.findByCategoryName(categoryName);
        List<Catalogue> catalogues = new ArrayList<>();
        for (Category category : categories) {
            List<Catalogue> associatedCatalogues = catalogueRepository.findByCategoriesContaining(category);
            catalogues.addAll(associatedCatalogues);
        }
        return catalogues;
    }

    @Override
    public void validateAndDeleteCatalogue(Long id) {
        Catalogue catalogue = getCatalogueById(id);
        if (!catalogue.getProducts().isEmpty()) {
            throw new IllegalStateException("Cannot delete catalogue with active products.");
        }
        catalogueRepository.delete(catalogue);
    }

    @Override
    public void notifyOnCatalogueUpdate(Long id) {
        Catalogue catalogue = getCatalogueById(id);

        // Fetch products in the updated catalogue
        List<Product> products = new ArrayList<>();
        for (Category category : catalogue.getCategories()) {
            products.addAll(productRepository.findByCategoryId(category.getCategoryId()));
        }

        // Fetch customers who like these products.
        Set<Long> productIds = products.stream()
                .map(Product::getId)
                .collect(Collectors.toSet());

        List<Customer> interestedCustomers = customerRepository.findCustomersByLikedProducts(productIds);

        // Send notifications to these customers.
        String subject = "Catalogue Update Notification.";
        String bodyTemplate = "Dear Customer,\n\nThe catalogue '%s' has been updated. Please check the latest details on our platform.\n\nBest Regards,\nSkyMarket Team";

        for (Customer customer : interestedCustomers) {
            for (Product product : products) {
                if (customer.getLikedProducts().contains(product)) {
                    String body = String.format(bodyTemplate, customer.getFullName(), product.getProductName(), catalogue.getName());
                    notificationService.sendMail(customer.getEmail(), subject, body);
                }
            }
        }

        System.out.println("Notifications sent for catalogue update: " + catalogue.getName());
    }

}