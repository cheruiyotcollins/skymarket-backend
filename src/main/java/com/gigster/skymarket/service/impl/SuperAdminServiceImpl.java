package com.gigster.skymarket.service.impl;

import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.dto.SuperAdminDto;
import com.gigster.skymarket.mapper.MyDtoMapper;
import com.gigster.skymarket.mapper.ResponseDtoMapper;
import com.gigster.skymarket.service.SuperAdminService;
import com.gigster.skymarket.utils.DateUtils;

import com.gigster.skymarket.exception.ResourceNotFoundException;
import com.gigster.skymarket.repository.*;
import com.gigster.skymarket.dto.ExtendedResDto;
import com.gigster.skymarket.model.SuperAdmin;
import com.gigster.skymarket.utils.PhoneNumberEditor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class SuperAdminServiceImpl implements SuperAdminService {

    private final ProcRepository procRepository;

    private final SuperAdminRepo superAdminRepo;

    @Autowired
    ResponseDtoMapper responseDtoSetter;

    @Override
    public ResponseEntity<ResponseDto> addSuperAdmin(SuperAdminDto newSuperAdmin){
        try {
            SuperAdmin superAdmin = SuperAdmin.builder()
                    .email(newSuperAdmin.getEmail())
                    .superAdminName(newSuperAdmin.getSuperAdminName())
                    .contact(newSuperAdmin.getContact())
                    .build();

            superAdminRepo.save(superAdmin);
            return responseDtoSetter.responseDtoSetter(HttpStatus.CREATED,"Super admin added successfully.",newSuperAdmin);
        } catch (Exception e) {
            return responseDtoSetter.responseDtoSetter(HttpStatus.BAD_REQUEST,"Something went wrong, please check your request and try again.");
        }

    }

    @Override
    public ExtendedResDto getSuperAdminById(Long id) {
        SuperAdmin superAdmin = superAdminRepo.findById(id).orElseThrow(()
                ->new ResourceNotFoundException("Admin", "id", id));
        SuperAdminDto superAdminDto =  MyDtoMapper.mapDtoToClass(superAdmin, SuperAdminDto.class);
        return  ExtendedResDto.builder()
                .status(200)
                .body(superAdminDto)
                .message("Found.")
                .build();
    }

    @Override
    public ExtendedResDto getAllSuperAdmins() {
        List<SuperAdmin> supers = procRepository.getSuperAdmins();
        return  ExtendedResDto.builder()
                .status(200)
                .body(supers)
                .message("A list of all super admins.")
                .build();
    }

    @Override
    public ExtendedResDto updateSuperAdminById(SuperAdminDto superAdminDto) {
        log.info("admin id: {}", superAdminDto.getSuperAdminId());
        SuperAdmin existingSuperAdmin = superAdminRepo.findById(superAdminDto.getSuperAdminId()).get();
        log.info("admin id: {}", existingSuperAdmin.getSuperAdminId());

        existingSuperAdmin.setSuperAdminName(superAdminDto.getSuperAdminName());
        existingSuperAdmin.setContact(PhoneNumberEditor.resolveNumber(superAdminDto.getContact()));
        existingSuperAdmin.setCreatedOn(DateUtils.dateNowString());
        existingSuperAdmin.setEmail(superAdminDto.getEmail());
        existingSuperAdmin.setEmployeeNo(superAdminDto.getEmployeeNo());

        SuperAdmin newSuperAdmin = superAdminRepo.save(existingSuperAdmin);

        SuperAdminDto superAdminDto1= MyDtoMapper.mapDtoToClass(newSuperAdmin, SuperAdminDto.class);
        return ExtendedResDto.builder()
                .status(200)
                .body(superAdminDto1)
                .message("Super admin updated successfully")
                .build();

    }

    @Override
    public ResponseEntity<ResponseDto> deleteSuperAdminById(Long id) {
        SuperAdmin existingSuperAdmin= superAdminRepo.findById(id).orElseThrow(()->
                new ResourceNotFoundException("User","id", id));

        superAdminRepo.delete(existingSuperAdmin);
        return null;
    }

}