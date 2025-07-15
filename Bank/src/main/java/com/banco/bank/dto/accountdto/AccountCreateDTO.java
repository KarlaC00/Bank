package com.banco.bank.dto.accountdto;

import com.banco.bank.model.ExemptGMF;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AccountCreateDTO {
    @NotBlank(message = "Can't be Null")
    @Pattern(regexp = "\\d+", message = "Only numbers are allowed and positive")
    private String identificationNumber;
    private String accountNumber;
    private ExemptGMF exemptGMF;
}
