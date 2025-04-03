package com.doorway.Payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginPayload {
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Password is mandatory")
    private String password;
}
