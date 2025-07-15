package com.banco.bank.dto.accountdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OperationAccountDTO {
    @NotBlank(message = "Not null")
    @Pattern(regexp = "\\d+", message = "Only numbers are allowed and positive")
    private String accountNumber;
    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be a positive value")
    private Double amount;
}

