package com.banco.bank.dto.clientdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UpdateClientDTO {
    @NotBlank(message = "Can't be null or with empty spaces")
    @Size(min = 2)
    private String names;
    @NotBlank(message = "Can't be null or with empty spaces")
    @Size(min = 2)
    private String lastName;
    @Email
    @NotBlank(message = "Can't be null or with empty spaces")
    private String email;
}