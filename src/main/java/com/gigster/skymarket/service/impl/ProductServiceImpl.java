package com.gigster.skymarket.service.impl;

import com.gigster.skymarket.dto.NewProductDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.model.Category;
import com.gigster.skymarket.model.Product;
import com.gigster.skymarket.repository.CategoryRepository;
import com.gigster.skymarket.repository.ProductRepository;
import com.gigster.skymarket.service.ProductService;
import com.gigster.skymarket.setter.ResponseDtoSetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ResponseDtoSetter responseDtoSetter;
    @Override
    public ResponseEntity<ResponseDto> createProduct(NewProductDto newProductDto) {
        try {
            Product product = setProduct(newProductDto);
            return responseDtoSetter.responseDtoSetter(HttpStatus.CREATED, "Product Added Successfully",productRepository.save(product));


        } catch (Exception e) {
             return responseDtoSetter.responseDtoSetter(HttpStatus.BAD_REQUEST, "Something went wrong!!! please check your request and try again:>>>"+ e);

        }
    }
    @Override
    public ResponseEntity<ResponseDto>getAllProducts() {
        //todo pagination
        //todo map Product to product dto
        return responseDtoSetter.responseDtoSetter(HttpStatus.OK, "Fetched List of All Products",productRepository.findAll());

    }
    @Override
    public ResponseEntity<ResponseDto>getProductById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isPresent()){
            return responseDtoSetter.responseDtoSetter(HttpStatus.FOUND, "Product Fetched Successfully",optionalProduct.get());

        }else{
            return responseDtoSetter.responseDtoSetter(HttpStatus.NOT_FOUND, "Product not found with ID: "+ id);

        }
    }
    @Override
    public ResponseEntity<ResponseDto> updateProduct(Long id, NewProductDto newProductDto) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = setProduct(newProductDto);
            return responseDtoSetter.responseDtoSetter(HttpStatus.ACCEPTED, "product Updated successfully",productRepository.save(product));

        } else {
            return responseDtoSetter.responseDtoSetter(HttpStatus.NOT_FOUND, "Product not found with ID: "+ id);

        }

    }
    @Override
    public ResponseEntity<ResponseDto> deleteProduct(Long id) {

        try{
            productRepository.deleteById(id);
            return responseDtoSetter.responseDtoSetter(HttpStatus.ACCEPTED, "Product Deleted Successfully");

        }catch (Exception e){
            return responseDtoSetter.responseDtoSetter(HttpStatus.NOT_FOUND, "Product With Provided id not Found. err>>>   "+ e);
        }

    }

    private Product setProduct(NewProductDto newProductDto){
        Product product = Product.builder()
                .productName(newProductDto.getProductName())
                .stock(newProductDto.getStock())
                .price(newProductDto.getPrice())
                .manufacturer(newProductDto.getManufacturer())
                .build();
        // if mandatory is present then set it
        Optional<Category> category = categoryRepository.findById(newProductDto.getCategory_id());
        category.ifPresent(product::setCategory);
        return product;
    }
}
