package com.gigster.skymarket.service;

import com.gigster.skymarket.dto.AdminDto;
import com.gigster.skymarket.dto.ExtendedResDto;
import com.gigster.skymarket.dto.ResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface AdminService {
    ResponseEntity<ResponseDto> addAdmin(AdminDto newAdmin);

    ExtendedResDto getAdminById(Long id);

    ExtendedResDto getAllAdmins(int page, int size, String sort);

    ExtendedResDto updateAdminById(AdminDto adminDto);

    ResponseEntity<ResponseDto> deleteAdminById(Long id);
}
