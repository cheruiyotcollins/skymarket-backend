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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
            return responseDtoSetter.responseDtoSetter(HttpStatus.FOUND, "Product Fetched Successfully", productDto);
        } else {
            return responseDtoSetter.responseDtoSetter(HttpStatus.NOT_FOUND, "Product not found with ID: " + id);
        }
    }

    @Override
    public ResponseEntity<ResponseDto> getAllProducts(Pageable pageable) {

        Page<Product> productsPage = productRepository.findAll(pageable);

        List<ProductDto> ProductDtos = productsPage.getContent()
                .stream()
                .map(productMapper::toDto) // Use a mapper for consistency
                .collect(Collectors.toList());

        ResponseDto responseDto = ResponseDto.builder()
                .status(HttpStatus.OK)
                .description("List of All Products.")
                .payload(ProductDtos)
                .totalPages(productsPage.getTotalPages())
                .totalElements(productsPage.getTotalElements())
                .currentPage(productsPage.getNumber())
                .pageSize(productsPage.getSize())
                .build();

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

    private Product setProduct(NewProductDto newProductDto) {
        Product product = Product.builder()
                .productName(newProductDto.getProductName())
                .stock(newProductDto.getStock())
                .price(newProductDto.getPrice())
                .manufacturer(newProductDto.getManufacturer())
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