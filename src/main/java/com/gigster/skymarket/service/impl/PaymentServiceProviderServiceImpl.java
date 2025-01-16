package com.gigster.skymarket.service.impl;

import com.gigster.skymarket.dto.PaymentServiceProviderDto;
import com.gigster.skymarket.mapper.PaymentServiceProviderMapper;
import com.gigster.skymarket.model.PaymentServiceProvider;
import com.gigster.skymarket.repository.PaymentServiceProviderRepository;
import com.gigster.skymarket.service.PaymentServiceProviderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceProviderServiceImpl implements PaymentServiceProviderService {

    private final PaymentServiceProviderRepository repository;

    @Autowired
    public PaymentServiceProviderServiceImpl(PaymentServiceProviderRepository repository) {
        this.repository = repository;
    }

    @Override
    public PaymentServiceProviderDto create(PaymentServiceProviderDto paymentServiceProviderDto) {
        // Convert DTO to Entity
        PaymentServiceProvider entity = PaymentServiceProviderMapper.toEntity(paymentServiceProviderDto);

        // Validate unique fields
        if (repository.findByServiceProviderName(entity.getServiceProviderName()).isPresent()) {
            throw new IllegalArgumentException("Service Provider Name already exists");
        }
        if (repository.findByShortCode(entity.getShortCode()).isPresent()) {
            throw new IllegalArgumentException("Short Code already exists");
        }

        // Save entity to the database
        PaymentServiceProvider savedEntity = repository.save(entity);

        // Convert saved entity back to DTO
        return PaymentServiceProviderMapper.toDTO(savedEntity);
    }

    @Override
    public PaymentServiceProviderDto update(long id, PaymentServiceProviderDto paymentServiceProviderDto) {
        // Find the existing entity
        PaymentServiceProvider existingEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PaymentServiceProvider not found with id: " + id));


        existingEntity.setServiceProviderName(paymentServiceProviderDto.getServiceProviderName());
        existingEntity.setShortCode(paymentServiceProviderDto.getShortCode());

        PaymentServiceProvider updatedEntity = repository.save(existingEntity);

        // Convert updated entity to DTO
        return PaymentServiceProviderMapper.toDTO(updatedEntity);
    }

    @Override
    public PaymentServiceProviderDto getById(long id) {

        PaymentServiceProvider entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PaymentServiceProvider not found with id: " + id));
        return PaymentServiceProviderMapper.toDTO(entity);
    }

    @Override
    public List<PaymentServiceProviderDto> getAll() {
        return repository.findAll()
                .stream()
                .map(PaymentServiceProviderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("PaymentServiceProvider not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
