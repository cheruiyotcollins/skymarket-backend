package com.gigster.skymarket.service.impl;

import com.gigster.skymarket.dto.AttributeDto;
import com.gigster.skymarket.model.Attribute;
import com.gigster.skymarket.model.Product;
import com.gigster.skymarket.repository.AttributeRepository;
import com.gigster.skymarket.repository.ProductRepository;
import com.gigster.skymarket.service.AttributeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttributeServiceImpl implements AttributeService {

    private final AttributeRepository attributeRepository;
    private final ProductRepository productRepository;

    public AttributeServiceImpl(AttributeRepository attributeRepository, ProductRepository productRepository) {
        this.attributeRepository = attributeRepository;
        this.productRepository = productRepository;
    }

    @Override
    public AttributeDto addAttribute(Long productId, AttributeDto attributeDto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Attribute attribute = Attribute.builder()
                .name(attributeDto.getName())
                .value(attributeDto.getValue())
                .product(product)
                .build();

        Attribute savedAttribute = attributeRepository.save(attribute);
        return convertToDto(savedAttribute);
    }

    @Override
    public List<AttributeDto> getAttributesByProductId(Long productId) {
        return attributeRepository.findByProductId(productId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAttribute(Long id) {
        attributeRepository.deleteById(id);
    }

    private AttributeDto convertToDto(Attribute attribute) {
        return AttributeDto.builder()
                .id(attribute.getId())
                .name(attribute.getName())
                .value(attribute.getValue())
                .build();
    }
}
