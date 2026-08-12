package com.abtd.solarbackend.user.dto.request;

import com.abtd.solarbackend.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeRoleRequest {

    @NotNull(message = "Role is required")
    private UserRole role;
}