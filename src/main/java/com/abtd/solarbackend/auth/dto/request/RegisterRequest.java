package com.abtd.solarbackend.auth.dto.request;

import com.abtd.solarbackend.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "First name is required")
    @Size(max = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50)
    private String lastName;

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 30)
    private String username;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(regexp = "^[0-9]{10}$",
            message = "Mobile number must be 10 digits")
    private String mobile;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 20)
    private String password;

    @NotNull(message = "Role is required")
    private UserRole role;
}