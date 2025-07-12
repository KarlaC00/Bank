package com.banco.bank.dto.accountdto;

import com.banco.bank.model.StatusAccount;
import lombok.Data;

@Data
public class AccountUpdateDTO {
    private StatusAccount status;
}

