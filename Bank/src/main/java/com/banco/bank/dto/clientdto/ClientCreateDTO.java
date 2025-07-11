package com.banco.bank.dto.clientdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ClientCreateDTO {
    @NotBlank(message = "Can't be null or with empty spaces")
    private String identificationType;
    @NotBlank(message = "Can't be null or with empty spaces")
    @Pattern(regexp = "\\d+", message = "Only numbers are allowed")
    private String identificationNumber;
    @NotBlank(message = "Can't be null or with empty spaces")
    private String names;
    @NotBlank(message = "Can't be null or with empty spaces")
    private String lastName;
    @NotBlank(message = "Can't be null or with empty spaces")
    private String email;
    @PastOrPresent
    @NotNull
    private LocalDate birthDate;
}