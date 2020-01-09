package com.moneytransfer.model.dto;

import com.moneytransfer.model.enums.Currency;
import com.moneytransfer.model.enums.TransactionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class TransferDTO {
    @Positive(message = "Sender account id must be positive")
    @NotNull(message = "Sender account id must be specified")
    private Long senderAccountId;

    @Positive(message = "Recipient account id must be positive")
    @NotNull(message = "Recipient account id must be specified")
    private Long recipientAccountId;

    @Positive(message = "Amount must be positive")
    @NotNull(message = "Amount must be specified")
    private BigDecimal amount;

    @NotNull(message = "Currency must be specified")
    private Currency currency;

    @NotNull(message = "Transaction type must be specified")
    private TransactionType transactionType;
}
