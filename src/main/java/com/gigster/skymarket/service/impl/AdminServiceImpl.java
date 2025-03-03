package com.gigster.skymarket.service.impl;

import com.gigster.skymarket.dto.AdminDto;
import com.gigster.skymarket.dto.ExtendedResDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.exception.ResourceNotFoundException;
import com.gigster.skymarket.mapper.MyDtoMapper;
import com.gigster.skymarket.mapper.ResponseDtoMapper;
import com.gigster.skymarket.model.Admin;
import com.gigster.skymarket.repository.AdminRepository;
import com.gigster.skymarket.repository.ProcRepository;
import com.gigster.skymarket.service.AdminService;
import com.gigster.skymarket.utils.PhoneNumberEditor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    private final ProcRepository procRepository;

    @Autowired
    ResponseDtoMapper responseDtoSetter;

    @Override
    public ResponseEntity<ResponseDto> addAdmin(AdminDto newAdmin){
        try {
            Admin admin = Admin.builder()
                    .email(newAdmin.getEmail())
                    .fullName(newAdmin.getFullName())
                    .contact(newAdmin.getContact())
                    .build();

            adminRepository.save(admin);
            return responseDtoSetter.responseDtoSetter(HttpStatus.CREATED,"Customer added successfully.",newAdmin);
        } catch (Exception e) {
            return responseDtoSetter.responseDtoSetter(HttpStatus.BAD_REQUEST,"Something went wrong, please check your request and try again.");
        }

    }

    @Override
    public ExtendedResDto getAdminById(Long id) {
        Admin admin = adminRepository.findById(id).orElseThrow(()
                ->new ResourceNotFoundException("Admin", "id", id));
        AdminDto adminDto =  MyDtoMapper.mapDtoToClass(admin, AdminDto.class);
        return  ExtendedResDto.builder()
                .status(200)
                .body(adminDto)
                .message("Found.")
                .build();
    }

    @Override
    public ExtendedResDto getAllAdmins(int page, int size, String sort) {
        String[] sortParams = sort.split(",");
        Sort.Direction direction = Sort.Direction.ASC; // Default sorting order.

        if (sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParams[0]));

        Page<Admin> adminsPage = procRepository.getAdmins(pageable);

        return ExtendedResDto.builder()
                .status(200)
                .body(adminsPage.getContent())
                .message("A paginated list of admins.")
                .build();
    }

    @Override
    public ExtendedResDto updateAdminById(AdminDto adminDto) {
        log.info("admin id: {}", adminDto.getAdminId());
        Admin existingAdmin = adminRepository.findById(adminDto.getAdminId()).get();
        log.info("admin id: {}", existingAdmin.getAdminId());

        existingAdmin.setFullName(adminDto.getFullName());
        existingAdmin.setContact(PhoneNumberEditor.resolveNumber(adminDto.getContact()));
        existingAdmin.setCreatedOn(Instant.now());
        existingAdmin.setEmail(adminDto.getEmail());

        Admin newAdmin = adminRepository.save(existingAdmin);

        AdminDto adminDto1= MyDtoMapper.mapDtoToClass(newAdmin, AdminDto.class);
        return ExtendedResDto.builder()
                .status(200)
                .body(adminDto1)
                .message("Admin updated successfully")
                .build();

    }

    @Override
    public ResponseEntity<ResponseDto> deleteAdminById(Long id) {
        Admin existingAdmin= adminRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("User","id", id));

        adminRepository.delete(existingAdmin);
        return null;
    }

}
