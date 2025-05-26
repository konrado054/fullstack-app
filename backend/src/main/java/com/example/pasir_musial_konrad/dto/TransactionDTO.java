package com.example.pasir_musial_konrad.dto;

import com.example.pasir_musial_konrad.model.TransactionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
public class TransactionDTO {

    @Setter
    @NotNull(message = "Kwota nie moze byc pusta!")
    @Min(value = 1, message = "Kwota musi byc wieksza niz 0")
    private Double amount;

    @Setter
    @NotNull(message = "Pole typu transakcji musi być wypełnione!")
    @Pattern(regexp = "^(INCOME|EXPENSE)$", message = "Pole musi zawierać wartość PRZYCHÓD lub WYDATEK!")
    private String type;

    @Setter
    @Size(max = 50, message = "Tagi mogą mieć maksymalnie 50 znaków!")
    private String tags;

    @Setter
    @Size(max = 255, message = "Tagi mogą miećmaksymalnie 255 znaków!")
    private String notes;


    private LocalDateTime timestamp;

}
