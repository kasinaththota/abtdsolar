package com.abtd.solarbackend.user.dto.request;

import com.abtd.solarbackend.enums.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeStatusRequest {

    @NotNull(message = "Status is required")
    private UserStatus status;
}