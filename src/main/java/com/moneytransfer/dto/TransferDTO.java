package com.moneytransfer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
