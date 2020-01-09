package com.moneytransfer.service;

import com.google.inject.Inject;
import com.moneytransfer.configuration.googlejuice.aspect.InTransaction;
import com.moneytransfer.exception.MoneyTransferException;
import com.moneytransfer.model.dto.AccountCreationDTO;
import com.moneytransfer.model.dto.TransferDTO;
import com.moneytransfer.model.dto.entity.AccountDTO;
import com.moneytransfer.model.dto.entity.AccountTransactionDTO;
import com.moneytransfer.model.entity.Account;
import org.eclipse.jetty.http.HttpStatus;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Objects;

public class AccountService {
    private final AccountTransactionService accountTransactionService;
    private final UserService userService;

    @Inject
    public AccountService(AccountTransactionService accountTransactionService, UserService userService) {
        this.accountTransactionService = accountTransactionService;
        this.userService = userService;
    }

    @InTransaction
    public AccountDTO create(AccountCreationDTO accountCreationDTO) {
        Long userId = accountCreationDTO.getUserId();

        userService.validateUserExists(userId);
        validateAccountDoesntExist(userId);

        Account account = new Account(
                userId,
                BigDecimal.ZERO,
                accountCreationDTO.getCurrency().name(),
                LocalDateTime.now()
        );

        account.saveIt();

        return toDTO(account);
    }

    private void validateAccountDoesntExist(Long userId) {
        if (!Account.where("user_id = ?", userId).isEmpty())
            throw new MoneyTransferException("User with specified id already has account", HttpStatus.NOT_ACCEPTABLE_406);
    }

    @InTransaction
    public AccountTransactionDTO transfer(TransferDTO transferDTO) {
        Account recipient = Account.findById(transferDTO.getRecipientAccountId());
        Account sender = Account.findById(transferDTO.getSenderAccountId());

        validate(recipient, sender, transferDTO.getAmount());

        withdraw();
        deposit();

        return accountTransactionService.create();
    }

    private void validate(Account recipient, Account sender, BigDecimal amount) {
        if (Objects.isNull(recipient) || Objects.isNull(sender)) {
            throw new MoneyTransferException("Recipient or sender doesn't exist", HttpStatus.NOT_ACCEPTABLE_406);
        }

        BigDecimal balance = sender.getBalance();
        if (balance.compareTo(amount) <= 0) {
            throw new MoneyTransferException("Transfer not allowed insufficient funds", HttpStatus.NOT_ACCEPTABLE_406);
        }
    }

    private void deposit() {

    }

    private void withdraw() {

    }

    private AccountDTO toDTO(Account account) {
        return AccountDTO.builder()
                .userId(account.getUserId())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .dateUpdated(account.getDateUpdated())
                .dateCreated(account.getDateCreated())
                .build();
    }
}
