package com.gigster.skymarket.service;

import com.gigster.skymarket.dto.NewProductDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.model.Category;
import com.gigster.skymarket.model.Product;
import com.gigster.skymarket.repository.CategoryRepository;
import com.gigster.skymarket.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @Autowired
    CategoryRepository categoryRepository;
    ResponseDto responseDto;

    public ResponseEntity<ResponseDto> createProduct(NewProductDto newProductDto) {
        responseDto= new ResponseDto();
        try {
            Product product = new Product();
            product.setProductName(newProductDto.getProductName());
            product.setManufacturer(newProductDto.getManufacturer());
            product.setPrice(newProductDto.getPrice());
            product.setStock(newProductDto.getStock());
            Optional<Category> category = categoryRepository.findById(newProductDto.getCategory_id());
            category.ifPresent(product::setCategory);
            responseDto.setStatus(HttpStatus.CREATED);
            responseDto.setDescription("Product Added Successfully");
            responseDto.setPayload(productRepository.save(product));

        } catch (Exception e) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setDescription("Something went wrong!!! please check your request and try again:>>>"+ e);

        }

        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    public ResponseEntity<ResponseDto>getAllProducts() {
        responseDto= new ResponseDto();
        responseDto.setStatus(HttpStatus.OK);
        responseDto.setDescription("Fetched List of All Products");
        responseDto.setPayload(productRepository.findAll());
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    public ResponseEntity<ResponseDto>getProductById(Long id) {
        responseDto= new ResponseDto();
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isPresent()){
            responseDto.setStatus(HttpStatus.FOUND);
            responseDto.setDescription("Product With Provided Fetched Successfully");
            responseDto.setPayload(optionalProduct.get());
        }else{
            responseDto.setStatus(HttpStatus.FOUND);
            responseDto.setDescription("Product not found with ID: "+ id);
        }
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    public ResponseEntity<ResponseDto> updateProduct(Long id, NewProductDto newProductDto) {
        responseDto= new ResponseDto();
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setProductName(newProductDto.getProductName());
            product.setManufacturer(newProductDto.getManufacturer());
            product.setPrice(newProductDto.getPrice());
            product.setStock(newProductDto.getStock());
            Optional<Category> category=categoryRepository.findById(newProductDto.getCategory_id());
            category.ifPresent(product::setCategory);
             responseDto.setStatus(HttpStatus.ACCEPTED);
             responseDto.setDescription("product Updated successfully");
             responseDto.setPayload(productRepository.save(product));
        } else {
            responseDto.setStatus(HttpStatus.NOT_FOUND);
            responseDto.setDescription("Product not found with ID:  " + id);

        }
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    public ResponseEntity<ResponseDto> deleteProduct(Long id) {
        responseDto= new ResponseDto();
        try{
            productRepository.deleteById(id);
            responseDto.setStatus(HttpStatus.ACCEPTED);
            responseDto.setDescription("Product Deleted Successfully");

        }catch (Exception e){
            responseDto.setStatus(HttpStatus.NOT_FOUND);
            responseDto.setDescription("Product With Provided id not Found. err>>>   "+ e);
        }
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }
}
