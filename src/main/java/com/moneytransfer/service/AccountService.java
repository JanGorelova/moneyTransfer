package com.moneytransfer.service;

import com.google.inject.Inject;
import com.moneytransfer.configuration.googlejuice.aspect.InTransaction;
import com.moneytransfer.exception.MoneyTransferException;
import com.moneytransfer.model.dto.AccountCreationDTO;
import com.moneytransfer.model.dto.TransferDTO;
import com.moneytransfer.model.dto.entity.AccountDTO;
import com.moneytransfer.model.dto.entity.AccountTransactionDTO;
import com.moneytransfer.model.entity.Account;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
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
        checkAccountDoesntExist(userId);

        Account account = new Account(
                userId,
                BigDecimal.ZERO,
                accountCreationDTO.getCurrency().name(),
                LocalDateTime.now()
        );

        account.saveIt();

        return toDTO(account);
    }

    @InTransaction
    public AccountTransactionDTO transfer(TransferDTO transferDTO) {
        Account recipient = Account.findById(transferDTO.getRecipientAccountId());
        Account sender = Account.findById(transferDTO.getSenderAccountId());

        BigDecimal amount = transferDTO.getAmount();
        checkEnoughMoney(recipient, sender, amount);

        withdraw(sender, amount);
        deposit(recipient,amount);

        log.info(String.format("Amount %s of %s was transferred from sender with id: %s to recipient with id: %s",
                amount, transferDTO.getCurrency() ,sender.getId(), recipient.getId()));

        return accountTransactionService.create(transferDTO);
    }

    private void checkAccountDoesntExist(Long userId) {
        if (!Account.where("user_id = ?", userId).isEmpty())
            throw new MoneyTransferException("User with specified id already has account", HttpStatus.NOT_ACCEPTABLE_406);
    }

    private void checkEnoughMoney(Account recipient, Account sender, BigDecimal amount) {
        if (Objects.isNull(recipient) || Objects.isNull(sender))
            throw new MoneyTransferException("Recipient or sender doesn't exist", HttpStatus.NOT_ACCEPTABLE_406);

        BigDecimal balance = sender.getBalance();
        if (balance.compareTo(amount) <= 0) {
            throw new MoneyTransferException("Transfer not allowed insufficient funds", HttpStatus.NOT_ACCEPTABLE_406);
        }
    }

    private void deposit(Account recipient, BigDecimal amount) {
        BigDecimal previousBalance = recipient.getBalance();

        recipient.setBigDecimal("balance", previousBalance.add(amount)).saveIt();
    }

    private void withdraw(Account sender, BigDecimal amount) {
        BigDecimal previousBalance = sender.getBalance();

        sender.setBigDecimal("balance", previousBalance.subtract(amount)).saveIt();
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
