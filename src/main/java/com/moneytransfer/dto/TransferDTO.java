package com.moneytransfer.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class TransferDTO {
    @Positive
    @NotNull
    private Long senderAccountId;

    @Positive
    @NotNull
    private Long receiverAccountId;

    @Positive
    @NotNull
    private BigDecimal amount;
}
