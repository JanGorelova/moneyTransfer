package com.moneytransfer.model.dto.entity;

import com.moneytransfer.model.enums.Currency;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountDTO {
    private Long id;

    private Long userId;

    private BigDecimal balance;

    private Currency currency;

    private LocalDateTime dateCreated;

    private LocalDateTime dateUpdated;
}
