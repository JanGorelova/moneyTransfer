package com.moneytransfer.model.dto.request;

import com.moneytransfer.model.enums.Currency;
import com.moneytransfer.model.enums.TransactionType;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AccountTransferDTO {
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
