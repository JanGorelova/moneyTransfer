package com.moneytransfer.model.dto.entity;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private LocalDateTime dateCreated;

    private LocalDateTime dateUpdated;
}
