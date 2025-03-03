package com.gigster.skymarket.service.impl;

import com.gigster.skymarket.dto.PaymentServiceProviderDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.mapper.PaymentServiceProviderMapper;
import com.gigster.skymarket.model.PaymentServiceProvider;
import com.gigster.skymarket.repository.PaymentServiceProviderRepository;
import com.gigster.skymarket.service.PaymentServiceProviderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceProviderServiceImpl implements PaymentServiceProviderService {

    private final PaymentServiceProviderRepository repository;
    private final PaymentServiceProviderMapper mapper;

    @Autowired
    public PaymentServiceProviderServiceImpl(PaymentServiceProviderRepository repository, PaymentServiceProviderMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public PaymentServiceProviderDto create(PaymentServiceProviderDto paymentServiceProviderDto) {

        PaymentServiceProvider entity = mapper.toEntity(paymentServiceProviderDto);

        if (repository.findByServiceProviderName(entity.getServiceProviderName()).isPresent()) {
            throw new IllegalArgumentException("Service Provider Name already exists");
        }
        if (repository.findByShortCode(entity.getShortCode()).isPresent()) {
            throw new IllegalArgumentException("Short Code already exists");
        }

        PaymentServiceProvider savedEntity = repository.save(entity);

        return mapper.toDto(savedEntity);
    }

    @Override
    public PaymentServiceProviderDto update(long id, PaymentServiceProviderDto paymentServiceProviderDto) {
        PaymentServiceProvider existingEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PaymentServiceProvider not found with id: " + id));

        existingEntity.setServiceProviderName(paymentServiceProviderDto.getServiceProviderName());
        existingEntity.setShortCode(paymentServiceProviderDto.getShortCode());

        PaymentServiceProvider updatedEntity = repository.save(existingEntity);

        return mapper.toDto(updatedEntity);
    }

    @Override
    public PaymentServiceProviderDto getById(long id) {
        PaymentServiceProvider entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PaymentServiceProvider not found with id: " + id));
        return mapper.toDto(entity);
    }

    @Override
    public ResponseEntity<ResponseDto> getAllPSP(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort.split(",")));
        Page<PaymentServiceProvider> pspPage = repository.findAll(pageable);

        List<PaymentServiceProviderDto> dtos = pspPage.getContent()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        ResponseDto responseDto = ResponseDto.builder()
                .status(HttpStatus.OK)
                .description("List of All Payment Service Providers.")
                .payload(dtos)
                .totalPages(pspPage.getTotalPages())
                .totalElements(pspPage.getTotalElements())
                .currentPage(pspPage.getNumber())
                .pageSize(pspPage.getSize())
                .build();

        return ResponseEntity.ok(responseDto);
    }


    @Override
    public void deleteById(long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("PaymentServiceProvider not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
