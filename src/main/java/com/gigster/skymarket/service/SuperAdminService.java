package com.gigster.skymarket.service;

import com.gigster.skymarket.dto.ExtendedResDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.dto.SuperAdminDto;
import org.springframework.http.ResponseEntity;

public interface SuperAdminService {

    ResponseEntity<ResponseDto> addSuperAdmin(SuperAdminDto newSuperAdmin);

    ExtendedResDto getSuperAdminById(Long id);

    ExtendedResDto getAllSuperAdmins(int page, int size, String sort);

    ExtendedResDto updateSuperAdminById(SuperAdminDto superAdminDto);

    ResponseEntity<ResponseDto> deleteSuperAdminById(Long id);
}
