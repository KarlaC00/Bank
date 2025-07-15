package com.banco.bank.dto.accountdto;

import com.banco.bank.model.StatusAccount;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccountUpdateDTO {
    @NotBlank(message = "Can't be Null")
    private StatusAccount status;
}

