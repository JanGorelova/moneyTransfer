package com.moneytransfer.model.dto.entity;

import lombok.*;

import java.util.Optional;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExceptionDTO {
    private String message;

    private String causeMessage;

    public static ExceptionDTO getExceptionDTO(Exception exception) {
            return ExceptionDTO.builder()
                    .message(exception.getMessage())
                    .causeMessage(Optional
                            .ofNullable(exception.getCause())
                            .map(Throwable::getMessage)
                            .orElse("Not specified"))
                    .build();
    }
}
