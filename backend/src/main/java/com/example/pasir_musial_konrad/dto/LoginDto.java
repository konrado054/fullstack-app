package com.example.pasir_musial_konrad.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
