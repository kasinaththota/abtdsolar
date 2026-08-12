package com.abtd.solarbackend.vendor.controller;

import com.abtd.solarbackend.common.dto.PageRequestDto;
import com.abtd.solarbackend.common.dto.PageResponse;
import com.abtd.solarbackend.common.response.ApiResponse;
import com.abtd.solarbackend.common.response.ResponseBuilder;
import com.abtd.solarbackend.common.constants.VendorMessages;
import com.abtd.solarbackend.enums.VendorStatus;
import com.abtd.solarbackend.vendor.dto.request.CreateVendorRequest;
import com.abtd.solarbackend.vendor.dto.request.UpdateVendorRequest;
import com.abtd.solarbackend.vendor.dto.response.VendorResponse;
import com.abtd.solarbackend.vendor.service.VendorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendors")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','PURCHASE')")
public class VendorController {

    private final VendorService vendorService;

    @PostMapping
    public ResponseEntity<ApiResponse<VendorResponse>> createVendor(
            @Valid @RequestBody CreateVendorRequest request) {

        VendorResponse response = vendorService.createVendor(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseBuilder.success(
                        HttpStatus.CREATED.value(),
                        VendorMessages.VENDOR_CREATED,
                        response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VendorResponse>> getVendorById(
            @PathVariable Long id) {

        VendorResponse response = vendorService.getVendorById(id);

        return ResponseEntity.ok(
                ResponseBuilder.success(
                        HttpStatus.OK.value(),
                        VendorMessages.VENDOR_FETCHED,
                        response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<VendorResponse>>> getAllVendors(
            PageRequestDto pageRequest) {

        PageResponse<VendorResponse> response =
                vendorService.getAllVendors(pageRequest);

        return ResponseEntity.ok(
                ResponseBuilder.success(
                        HttpStatus.OK.value(),
                        VendorMessages.VENDORS_FETCHED,
                        response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VendorResponse>> updateVendor(
            @PathVariable Long id,
            @Valid @RequestBody UpdateVendorRequest request) {

        VendorResponse response =
                vendorService.updateVendor(id, request);

        return ResponseEntity.ok(
                ResponseBuilder.success(
                        HttpStatus.OK.value(),
                        VendorMessages.VENDOR_UPDATED,
                        response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteVendor(
            @PathVariable Long id) {

        vendorService.deleteVendor(id);

        return ResponseEntity.ok(
                ResponseBuilder.success(
                        HttpStatus.OK.value(),
                        VendorMessages.VENDOR_DELETED,
                        null));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<PageResponse<VendorResponse>>> getVendorsByStatus(
            @PathVariable VendorStatus status,
            PageRequestDto pageRequest) {

        PageResponse<VendorResponse> response =
                vendorService.getVendorsByStatus(status, pageRequest);

        return ResponseEntity.ok(
                ResponseBuilder.success(
                        HttpStatus.OK.value(),
                        VendorMessages.VENDORS_FETCHED,
                        response));
    }
}