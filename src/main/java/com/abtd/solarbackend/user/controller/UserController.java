package com.abtd.solarbackend.user.controller;

import com.abtd.solarbackend.common.response.ResponseBuilder;
import com.abtd.solarbackend.user.dto.request.ChangePasswordRequest;
import com.abtd.solarbackend.user.dto.request.ChangeRoleRequest;
import com.abtd.solarbackend.user.dto.request.ChangeStatusRequest;
import com.abtd.solarbackend.user.dto.request.CreateUserRequest;
import com.abtd.solarbackend.user.dto.request.UpdateUserRequest;
import com.abtd.solarbackend.user.dto.response.UserResponse;
import com.abtd.solarbackend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    @GetMapping
    public Object getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<UserResponse> users = userService.getAllUsers(PageRequest.of(page, size));

        return ResponseBuilder.ok(
                "Users fetched successfully",
                users
        );
    }

    @GetMapping("/{id}")
    public Object getUserById(@PathVariable Long id) {

        return ResponseBuilder.ok(
                "User fetched successfully",
                userService.getUserById(id)
        );
    }

    @PostMapping
    public Object createUser(@Valid @RequestBody CreateUserRequest request) {

        return ResponseBuilder.created(
                "User created successfully",
                userService.createUser(request)
        );
    }

    @PutMapping("/{id}")
    public Object updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {

        return ResponseBuilder.ok(
                "User updated successfully",
                userService.updateUser(id, request)
        );
    }

    @PutMapping("/{id}/role")
    public Object changeRole(
            @PathVariable Long id,
            @Valid @RequestBody ChangeRoleRequest request) {

        return ResponseBuilder.ok(
                "User role updated successfully",
                userService.changeRole(id, request)
        );
    }

    @PutMapping("/{id}/status")
    public Object changeStatus(
            @PathVariable Long id,
            @Valid @RequestBody ChangeStatusRequest request) {

        return ResponseBuilder.ok(
                "User status updated successfully",
                userService.changeStatus(id, request)
        );
    }

    @PutMapping("/{id}/password")
    public Object changePassword(
            @PathVariable Long id,
            @Valid @RequestBody ChangePasswordRequest request) {

        userService.changePassword(id, request);

        return ResponseBuilder.ok(
                "Password changed successfully",
                null
        );
    }

    @DeleteMapping("/{id}")
    public Object deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseBuilder.ok(
                "User deleted successfully",
                null
        );
    }
}