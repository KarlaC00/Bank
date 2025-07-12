package com.banco.bank.dto.accountdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OperationAccountDTO {
    @NotBlank(message = "Not null")
    private String accountNumber;
    @Positive
    @NotNull(message = "Not null")
    private Double amount;
}

