package com.gigster.skymarket.controller;

import com.gigster.skymarket.dto.*;
import com.gigster.skymarket.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/admins")
public class AdminController {

    private  final AdminService adminService;

    @PostMapping
    public ResponseEntity<ResponseDto> addAdmin(@RequestBody AdminDto admin){
        return adminService.addAdmin(admin);
    }

    @GetMapping("/{id}")
    public ExtendedResDto getAdminById(@PathVariable Long id) {
        return adminService.getAdminById(id);
    }

    @GetMapping
    public ResponseEntity<ExtendedResDto> getAllAdmins(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sort", defaultValue = "id,asc") String sort) {

        return ResponseEntity.ok(adminService.getAllAdmins(page, size, sort));
    }

    @PutMapping("/{id}")
    public ExtendedResDto  updateAdminById(
            @RequestBody AdminDto adminDto){
        return adminService.updateAdminById(adminDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteAdminById(@PathVariable long id) {
        return adminService.deleteAdminById(id);
    }

}