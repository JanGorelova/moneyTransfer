package com.moneytransfer.model.dto.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreationDTO {
    @NotEmpty(message = "First name must be specified")
    private String firstName;

    @NotEmpty(message = "Last name must be specified")
    private String lastName;

    @NotEmpty(message = "Email name must be specified")
    @Email(message = "Email must satisfy pattern")
    private String email;
}
