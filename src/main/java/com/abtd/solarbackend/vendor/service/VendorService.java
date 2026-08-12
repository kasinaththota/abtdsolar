package com.abtd.solarbackend.vendor.service;

import com.abtd.solarbackend.common.dto.PageRequestDto;
import com.abtd.solarbackend.common.dto.PageResponse;
import com.abtd.solarbackend.enums.VendorStatus;
import com.abtd.solarbackend.vendor.dto.request.CreateVendorRequest;
import com.abtd.solarbackend.vendor.dto.request.UpdateVendorRequest;
import com.abtd.solarbackend.vendor.dto.response.VendorResponse;

public interface VendorService {

    VendorResponse createVendor(CreateVendorRequest request);

    VendorResponse getVendorById(Long id);

    PageResponse<VendorResponse> getAllVendors(PageRequestDto pageRequest);

    VendorResponse updateVendor(Long id, UpdateVendorRequest request);

    void deleteVendor(Long id);

    PageResponse<VendorResponse> getVendorsByStatus(
            VendorStatus status,
            PageRequestDto pageRequest);
}