package com.abtd.solarbackend.vendor.service.impl;

import com.abtd.solarbackend.common.dto.PageRequestDto;
import com.abtd.solarbackend.common.dto.PageResponse;
import com.abtd.solarbackend.vendor.dto.request.CreateVendorRequest;
import com.abtd.solarbackend.vendor.dto.response.VendorResponse;
import com.abtd.solarbackend.vendor.entity.Vendor;
import com.abtd.solarbackend.vendor.mapper.VendorMapper;
import com.abtd.solarbackend.vendor.repository.VendorRepository;
import com.abtd.solarbackend.vendor.service.VendorService;
import com.abtd.solarbackend.common.response.PageResponseBuilder;
import com.abtd.solarbackend.common.constants.VendorMessages;
import com.abtd.solarbackend.enums.VendorStatus;
import com.abtd.solarbackend.vendor.dto.request.UpdateVendorRequest;
import com.abtd.solarbackend.vendor.exception.DuplicateVendorException;
import com.abtd.solarbackend.vendor.exception.VendorNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;
    private final VendorMapper vendorMapper;

    @Override
    public VendorResponse createVendor(CreateVendorRequest request) {

        validateDuplicateVendor(request);

        Vendor vendor = vendorMapper.toEntity(request);
        vendor.setVendorCode(generateVendorCode());

        return vendorMapper.toResponse(
                vendorRepository.save(vendor));
    }

    @Override
    @Transactional(readOnly = true)
    public VendorResponse getVendorById(Long id) {

        return vendorMapper.toResponse(findVendor(id));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<VendorResponse> getAllVendors(PageRequestDto pageRequest) {

        Pageable pageable = pageRequest.toPageable();

        Page<Vendor> page = vendorRepository.findAll(pageable);

        Page<VendorResponse> response =
                page.map(vendorMapper::toResponse);

        return PageResponseBuilder.build(response);
    }

    @Override
    public VendorResponse updateVendor(Long id,
                                       UpdateVendorRequest request) {

        Vendor vendor = findVendor(id);

        if (!vendor.getEmail().equalsIgnoreCase(request.getEmail())
                && vendorRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateVendorException(
                    VendorMessages.VENDOR_EMAIL_EXISTS);
        }

        if (!vendor.getMobile().equals(request.getMobile())
                && vendorRepository.existsByMobile(request.getMobile())) {
            throw new DuplicateVendorException(
                    VendorMessages.VENDOR_MOBILE_EXISTS);
        }

        if (!vendor.getGstNumber().equalsIgnoreCase(request.getGstNumber())
                && vendorRepository.existsByGstNumber(request.getGstNumber())) {
            throw new DuplicateVendorException(
                    VendorMessages.VENDOR_GST_EXISTS);
        }

        vendorMapper.updateEntity(request, vendor);

        return vendorMapper.toResponse(
                vendorRepository.save(vendor));
    }

    @Override
    public void deleteVendor(Long id) {

        Vendor vendor = findVendor(id);

        vendor.setStatus(VendorStatus.INACTIVE);

        vendorRepository.save(vendor);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<VendorResponse> getVendorsByStatus(
            VendorStatus status,
            PageRequestDto pageRequest) {

        Pageable pageable = pageRequest.toPageable();

        Page<Vendor> page =
                vendorRepository.findByStatus(status, pageable);

        Page<VendorResponse> response =
                page.map(vendorMapper::toResponse);

        return PageResponseBuilder.build(response);
    }

    private Vendor findVendor(Long id) {

        return vendorRepository.findById(id)
                .orElseThrow(() ->
                        new VendorNotFoundException(id));
    }

    private void validateDuplicateVendor(CreateVendorRequest request) {

        if (vendorRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateVendorException(
                    VendorMessages.VENDOR_EMAIL_EXISTS);
        }

        if (vendorRepository.existsByMobile(request.getMobile())) {
            throw new DuplicateVendorException(
                    VendorMessages.VENDOR_MOBILE_EXISTS);
        }

        if (vendorRepository.existsByGstNumber(request.getGstNumber())) {
            throw new DuplicateVendorException(
                    VendorMessages.VENDOR_GST_EXISTS);
        }
    }

    private String generateVendorCode() {

        return vendorRepository.findTopByOrderByIdDesc()
                .map(vendor -> {
                    int next = Integer.parseInt(
                            vendor.getVendorCode().substring(3)) + 1;
                    return String.format("VEN%06d", next);
                })
                .orElse("VEN000001");
    }
}