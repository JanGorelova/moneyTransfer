package com.moneytransfer.service;

import com.google.inject.Inject;
import com.moneytransfer.configuration.googlejuice.aspect.InTransaction;
import com.moneytransfer.exception.MoneyTransferException;
import com.moneytransfer.model.dto.entity.AccountDTO;
import com.moneytransfer.model.dto.entity.AccountTransactionDTO;
import com.moneytransfer.model.dto.request.AccountCreationDTO;
import com.moneytransfer.model.dto.request.AccountDepositDTO;
import com.moneytransfer.model.dto.request.AccountTransferDTO;
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

        userService.verifyUserExists(userId);
        verifyAccountDoesntExist(userId);

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
    public AccountTransactionDTO transfer(AccountTransferDTO transferDTO) {
        Account recipient = getAccount(transferDTO.getRecipientAccountId());
        Account sender = getAccount(transferDTO.getSenderAccountId());

        BigDecimal amount = transferDTO.getAmount();
        //todo add exchange
        verifyEnoughMoney(sender, amount);

        withdraw(sender, amount);
        deposit(recipient, amount);

        log.info(String.format("Amount %s of %s was transferred from sender with id: %s to recipient with id: %s",
                amount, transferDTO.getCurrency() ,sender.getId(), recipient.getId()));

        return accountTransactionService.create(transferDTO);
    }

    @InTransaction
    public AccountDTO deposit(AccountDepositDTO accountDepositDTO) {
        Account account = getAccount(accountDepositDTO.getRecipientAccountId());

        deposit(account, accountDepositDTO.getAmount());

        return toDTO(account);
    }

    private void deposit(Account recipient, BigDecimal amount) {
        BigDecimal recipientsPreviousBalance = recipient.getBalance();

        changeBalance(recipient, recipientsPreviousBalance.add(amount));
    }

    private void withdraw(Account sender, BigDecimal amount) {
        BigDecimal sendersPreviousBalance = sender.getBalance();

        changeBalance(sender, sendersPreviousBalance.subtract(amount));
    }

    private Account getAccount(Long accountId) {
        Account account = Account.findById(accountId);

        if (Objects.isNull(account))
            throw new MoneyTransferException(String.format("Account with id: %s doesn't exist", accountId), HttpStatus.NOT_ACCEPTABLE_406);

        return account;
    }

    private void changeBalance(Account account, BigDecimal amount) {
        account.setBigDecimal("balance", amount);
        account.setDate("date_updated", LocalDateTime.now());

        account.saveIt();
    }

    private void verifyAccountDoesntExist(Long userId) {
        if (!Account.where("user_id = ?", userId).isEmpty())
            throw new MoneyTransferException("User with specified id already has account", HttpStatus.NOT_ACCEPTABLE_406);
    }

    private void verifyEnoughMoney(Account sender, BigDecimal amount) {
        BigDecimal balance = sender.getBalance();
        if (balance.compareTo(amount) <= 0) {
            throw new MoneyTransferException("Transfer not allowed insufficient funds", HttpStatus.NOT_ACCEPTABLE_406);
        }
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
