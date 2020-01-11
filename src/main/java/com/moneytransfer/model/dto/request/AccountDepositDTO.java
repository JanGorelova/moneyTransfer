package com.moneytransfer.model.dto.request;

import com.moneytransfer.model.enums.Currency;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class AccountDepositDTO {
    @Positive(message = "Recipient account id must be positive")
    @NotNull(message = "Recipient account id must be specified")
    private Long recipientAccountId;

    @Positive(message = "Amount must be positive")
    @NotNull(message = "Amount must be specified")
    private BigDecimal amount;

    @NotNull(message = "Currency must be specified")
    private Currency currency;
}
