package com.moneytransfer.model.dto.entity;

import com.moneytransfer.model.enums.Currency;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
    private String firstName;

    private String lastName;

    private String email;

    private String dateCreated;

    private String dateUpdated;
}
