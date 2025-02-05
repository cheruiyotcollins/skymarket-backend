package com.gigster.skymarket.controller;

import com.gigster.skymarket.dto.AttributeDto;
import com.gigster.skymarket.service.AttributeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attributes")
public class AttributeController {

    private final AttributeService attributeService;

    public AttributeController(AttributeService attributeService) {
        this.attributeService = attributeService;
    }

    @PostMapping("/{productId}")
    public ResponseEntity<AttributeDto> addAttribute(@PathVariable Long productId, @RequestBody AttributeDto attributeDto) {
        return ResponseEntity.ok(attributeService.addAttribute(productId, attributeDto));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<List<AttributeDto>> getAttributes(@PathVariable Long productId) {
        return ResponseEntity.ok(attributeService.getAttributesByProductId(productId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAttribute(@PathVariable Long id) {
        attributeService.deleteAttribute(id);
        return ResponseEntity.ok("Attribute deleted successfully");
    }
}
