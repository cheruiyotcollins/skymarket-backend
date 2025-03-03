package com.gigster.skymarket.service.impl;

import com.gigster.skymarket.dto.NewProductDto;
import com.gigster.skymarket.dto.ProductDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.mapper.ProductMapper;
import com.gigster.skymarket.model.Category;
import com.gigster.skymarket.model.Product;
import com.gigster.skymarket.repository.CategoryRepository;
import com.gigster.skymarket.repository.ProductRepository;
import com.gigster.skymarket.service.ProductService;
import com.gigster.skymarket.mapper.ResponseDtoMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ResponseDtoMapper responseDtoSetter;
    private final ProductMapper productMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              ResponseDtoMapper responseDtoSetter,
                              ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.responseDtoSetter = responseDtoSetter;
        this.productMapper = productMapper;
    }

    @Override
    public ResponseEntity<ResponseDto> createProduct(NewProductDto newProductDto) {
        try {
            Product product = setProduct(newProductDto);
            return responseDtoSetter.responseDtoSetter(HttpStatus.CREATED, "Product Added Successfully", productRepository.save(product));
        } catch (Exception e) {
            return responseDtoSetter.responseDtoSetter(HttpStatus.BAD_REQUEST, "Something went wrong!!! Please check your request and try again: >>>" + e);
        }
    }

    @Override
    public ResponseEntity<ResponseDto> getProductById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product=optionalProduct.get();
            ProductDto productDto=productMapper.toDto(product);
            return responseDtoSetter.responseDtoSetter(HttpStatus.OK, "Product Fetched Successfully", productDto);
        } else {
            return responseDtoSetter.responseDtoSetter(HttpStatus.NOT_FOUND, "Product not found with ID: " + id);
        }
    }

    @Override
    public ResponseEntity<ResponseDto> getAllProducts(int page, int size, String sort) {
        String[] sortParams = sort.split(",");
        Sort.Direction direction = Sort.Direction.ASC;

        if (sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParams[0]));
        Page<Product> productsPage = productRepository.findAll(pageable);

        List<ProductDto> productDtos = productsPage.getContent()
                .stream()
                .map(productMapper::toDto)
                .toList();

        ResponseDto responseDto = responseDtoSetter.responseDtoSetter(
                HttpStatus.OK,
                "Fetched List of All Products.",
                productDtos
        ).getBody();

        assert responseDto != null;
        responseDto.setTotalPages(productsPage.getTotalPages());
        responseDto.setTotalElements(productsPage.getTotalElements());
        responseDto.setCurrentPage(productsPage.getNumber());
        responseDto.setPageSize(productsPage.getSize());

        return ResponseEntity.ok(responseDto);
    }

    private String generateImageUrl(String imageUrl) {
        if (imageUrl.startsWith("http")) {
            return imageUrl;
        }

        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/images/";
        return baseUrl + imageUrl;
    }

    @Override
    public ResponseEntity<ResponseDto> updateProduct(Long id, NewProductDto newProductDto) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = setProduct(newProductDto);
            return responseDtoSetter.responseDtoSetter(HttpStatus.ACCEPTED, "Product Updated successfully", productRepository.save(product));
        } else {
            return responseDtoSetter.responseDtoSetter(HttpStatus.NOT_FOUND, "Product not found with ID: " + id);
        }
    }

    @Override
    public ResponseEntity<ResponseDto> deleteProduct(Long id) {
        try {
            productRepository.deleteById(id);
            return responseDtoSetter.responseDtoSetter(HttpStatus.ACCEPTED, "Product Deleted Successfully");
        } catch (Exception e) {
            return responseDtoSetter.responseDtoSetter(HttpStatus.NOT_FOUND, "Product With Provided ID not Found. Err: >>> " + e);
        }
    }

    @Override
    public ResponseEntity<ResponseDto> restockProduct(Long id, int stock) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setStock(stock);
            productRepository.save(product);
            return responseDtoSetter.responseDtoSetter(HttpStatus.ACCEPTED, "Product restocked successfully", product);
        } else {
            return responseDtoSetter.responseDtoSetter(HttpStatus.NOT_FOUND, "Product not found with ID: " + id);
        }
    }

    @Override
    public ResponseEntity<ResponseDto> likeProduct(Long productId, Long userId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            if (product.getLikedByUsers() == null) {
                product.setLikedByUsers(new HashSet<>());
            }
            if (product.getLikedByUsers().contains(userId)) {
                return responseDtoSetter.responseDtoSetter(HttpStatus.BAD_REQUEST, "You have already liked this product.");
            }
            product.getLikedByUsers().add(userId);
            productRepository.save(product);
            return responseDtoSetter.responseDtoSetter(HttpStatus.OK, "Product liked successfully", product);
        } else {
            return responseDtoSetter.responseDtoSetter(HttpStatus.NOT_FOUND, "Product not found with ID: " + productId);
        }
    }

    @Override
    public ResponseEntity<ResponseDto> dislikeProduct(Long productId, Long userId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            if (product.getLikedByUsers() == null) {
                product.setLikedByUsers(new HashSet<>());
            }

            product.getLikedByUsers().remove(userId);

            productRepository.save(product);

            return responseDtoSetter.responseDtoSetter(HttpStatus.OK, "Product disliked successfully", product);
        } else {
            return responseDtoSetter.responseDtoSetter(HttpStatus.NOT_FOUND, "Product not found with ID: " + productId);
        }
}

    private Product setProduct(NewProductDto newProductDto) {
        Product product = Product.builder()
                .productName(newProductDto.getTitle())
                .imageUrl(newProductDto.getImage())
                .description(newProductDto.getDescription())
                .price(newProductDto.getPrice())
                .manufacturer(newProductDto.getManufacturer())
                .stock(0)
                .count(0)
                .rating(0.0)
                .build();
        // If mandatory is present then set it
        Optional<Category> category = categoryRepository.findById(newProductDto.getCategory_id());
        category.ifPresent(product::setCategory);
        return product;
    }

    // Method to map Product to ProductDto.
    private ProductDto mapToDto(Product product) {
        return ProductDto.builder()
                .title(product.getProductName())
                .category(product.getCategory().getCategoryName().name())
                .description(product.getDescription())
                .price(product.getPrice())
                .image(product.getImageUrl())
                .build();
    }

}