package com.moneytransfer.model.dto.entity;

import com.moneytransfer.model.enums.Currency;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountDTO {
    private Long userId;

    private BigDecimal balance;

    private Currency currency;

    private String dateCreated;

    private String dateUpdated;
}
