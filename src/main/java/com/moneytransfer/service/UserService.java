package com.moneytransfer.service;

import com.moneytransfer.configuration.googlejuice.aspect.InTransaction;
import com.moneytransfer.model.dto.UserCreationDTO;
import com.moneytransfer.model.dto.entity.UserDTO;
import com.moneytransfer.model.entity.User;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@NoArgsConstructor
public class UserService {
    @InTransaction
    public UserDTO create(UserCreationDTO userCreationDTO) {
        User user = new User(
                userCreationDTO.getFirstName(),
                userCreationDTO.getLastName(),
                userCreationDTO.getEmail(),
                LocalDateTime.now()
        );

        user.saveIt();

        return toDTO(user);
    }

    public void validateUserExists(Long userId) {

    }

    private UserDTO toDTO(User user) {
        return UserDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .dateCreated(user.getDateCreated())
                .dateUpdated(user.getDateUpdated())
                .build();
    }
}
