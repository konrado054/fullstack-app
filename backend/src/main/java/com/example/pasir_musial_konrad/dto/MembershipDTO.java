package com.example.pasir_musial_konrad.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
public class MembershipDTO {

    @NotNull(message = "Email uzytkownika nie moze byc puste")
    private String userEmail;

    @NotNull(message = "ID grupy nie moze byc puste")
    private Long groupId;


}
