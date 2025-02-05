package com.gigster.skymarket.service;

import com.gigster.skymarket.dto.AttributeDto;

import java.util.List;

public interface AttributeService {
    AttributeDto addAttribute(Long productId, AttributeDto attributeDto);

    List<AttributeDto> getAttributesByProductId(Long productId);

    void deleteAttribute(Long id);
}
