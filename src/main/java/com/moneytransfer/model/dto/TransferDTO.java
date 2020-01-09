package com.moneytransfer.model.dto;

import lombok.*;

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
}
