package com.moneytransfer.model.dto;

import com.moneytransfer.model.enums.Currency;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Date;

@Setter
@Getter
@NoArgsConstructor
public class UserCreationDTO {
    @NotEmpty(message = "First name must be specified")
    private String firstName;

    @NotEmpty(message = "Last name must be specified")
    private String lastName;

    @NotEmpty(message = "Email name must be specified")
    @Email(message = "Email must satisfy pattern")
    private String email;
}
