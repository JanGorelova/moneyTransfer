package com.moneytransfer.model.dto.entity;

import com.moneytransfer.model.enums.Currency;
import com.moneytransfer.model.enums.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountTransactionDTO {
    private Long senderId;

    private Long recipientId;

    private BigDecimal amount;

    private Currency currency;

    private TransactionType transactionType;

    private LocalDateTime dateCreated;
}
