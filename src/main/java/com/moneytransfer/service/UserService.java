package com.moneytransfer.service;

import com.moneytransfer.configuration.googlejuice.aspect.InTransaction;
import com.moneytransfer.exception.MoneyTransferException;
import com.moneytransfer.model.dto.entity.UserDTO;
import com.moneytransfer.model.dto.request.UserCreationDTO;
import com.moneytransfer.model.entity.User;
import lombok.NoArgsConstructor;
import org.eclipse.jetty.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
public class UserService {
    @InTransaction
    public UserDTO create(UserCreationDTO userCreationDTO) {
        verifyEmailIsUnique(userCreationDTO.getEmail());

        User user = new User(
                userCreationDTO.getFirstName(),
                userCreationDTO.getLastName(),
                userCreationDTO.getEmail(),
                LocalDateTime.now()
        );

        user.saveIt();

        return toDTO(user);
    }

    private void verifyEmailIsUnique(String email) {
        if (!User.where("email = ?", email).isEmpty())
            throw new MoneyTransferException(String.format("User with specified email: %s already exists", email), HttpStatus.NOT_ACCEPTABLE_406);
    }

    public void verifyUserExists(Long userId) {
        if (Objects.isNull(User.findById(userId)))
            throw new MoneyTransferException(String.format("User with specified id: %s doesn't exist", userId), HttpStatus.NOT_ACCEPTABLE_406);
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
