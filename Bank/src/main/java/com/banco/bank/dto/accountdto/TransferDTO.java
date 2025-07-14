package com.banco.bank.dto.accountdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferDTO {
    private String fromAccountNumber;
    private String toAccountNumber;
    private Double amount;
}
