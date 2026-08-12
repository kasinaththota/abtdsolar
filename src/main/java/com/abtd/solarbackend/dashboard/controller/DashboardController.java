package com.abtd.solarbackend.dashboard.controller;

import com.abtd.solarbackend.common.response.ResponseBuilder;
import com.abtd.solarbackend.dashboard.dto.response.DashboardResponse;
import com.abtd.solarbackend.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<?> getDashboard() {

        DashboardResponse response = dashboardService.getDashboard();

        return ResponseBuilder.ok(
                "Dashboard fetched successfully",
                response
        );
    }
}