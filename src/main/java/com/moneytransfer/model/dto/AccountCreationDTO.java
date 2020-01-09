package com.moneytransfer.model.dto;

import com.moneytransfer.model.enums.Currency;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
public class AccountCreationDTO {
    @NotNull(message = "User id must be specified")
    private Long userId;

    @NotNull(message = "Currency must be specified")
    private Currency currency;
}
