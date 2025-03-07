package com.gigster.skymarket.controller;

import com.gigster.skymarket.dto.*;
import com.gigster.skymarket.service.SuperAdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/super")
@CrossOrigin("*")
@AllArgsConstructor
public class SuperAdminController {

    private  final SuperAdminService superAdminService;

    @PostMapping
    public ResponseEntity<ResponseDto> addSuperAdmin(@RequestBody SuperAdminDto superAdmin){
        return superAdminService.addSuperAdmin(superAdmin);
    }

    @GetMapping("/{id}")
    public ExtendedResDto getSuperAdminById(@PathVariable Long id) {
        return superAdminService.getSuperAdminById(id);
    }

    @GetMapping
    public ExtendedResDto getAllSuperAdmins() {
        return superAdminService.getAllSuperAdmins();
    }

    @PutMapping("/{id}")
    public ExtendedResDto  updateAdminById(
            @RequestBody SuperAdminDto superAdminDto){
        return superAdminService.updateSuperAdminById(superAdminDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteSuperAdminById(@PathVariable long id) {
        return superAdminService.deleteSuperAdminById(id);
    }

}
