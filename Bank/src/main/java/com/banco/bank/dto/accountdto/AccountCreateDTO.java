package com.banco.bank.dto.accountdto;

import com.banco.bank.model.ExemptGMF;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AccountCreateDTO {
    @NotNull(message = "Can't be Null")
    private String identificationNumber;
    @NotNull(message = "Can't be Null")
    private String accountNumber;
    private ExemptGMF exemptGMF;
}
