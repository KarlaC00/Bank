package com.banco.bank.dto.accountdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferDTO {
    @Pattern(regexp = "\\d+", message = "Only numbers are and positive")
    @NotBlank(message = "Can't be Null")
    private String fromAccountNumber;
    @Pattern(regexp = "\\d+", message = "Only numbers are allowed and positive")
    @NotBlank(message = "Can't be Null")
    private String toAccountNumber;
    @Positive
    @NotNull
    private Double amount;
}
