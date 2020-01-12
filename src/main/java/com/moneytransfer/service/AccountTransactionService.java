package com.moneytransfer.service;

import com.moneytransfer.configuration.googlejuice.aspect.InTransaction;
import com.moneytransfer.configuration.googlejuice.aspect.ReadFromDatabase;
import com.moneytransfer.model.dto.entity.AccountTransactionDTO;
import com.moneytransfer.model.dto.request.AccountDepositDTO;
import com.moneytransfer.model.dto.request.AccountTransferDTO;
import com.moneytransfer.model.entity.AccountTransaction;
import com.moneytransfer.model.enums.TransactionType;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class AccountTransactionService {
    @InTransaction
    public AccountTransactionDTO create(AccountTransferDTO transferDTO) {
        AccountTransaction accountTransaction = new AccountTransaction(transferDTO.getSenderAccountId(),
                transferDTO.getRecipientAccountId(), transferDTO.getAmount(), transferDTO.getCurrency().name(),
                transferDTO.getTransactionType().name());

        accountTransaction.saveIt();

        return toDTO(accountTransaction);
    }

    @InTransaction
    public void createForDeposit(AccountDepositDTO accountDepositDTO) {
        AccountTransaction accountTransaction = new AccountTransaction(null, accountDepositDTO.getRecipientAccountId(),
                accountDepositDTO.getAmount(), accountDepositDTO.getCurrency().name(), TransactionType.DEPOSIT.name());

        accountTransaction.saveIt();
    }

    @ReadFromDatabase
    public List<AccountTransactionDTO> getAll() {
        return AccountTransaction.findAll().stream()
                .map(transaction -> toDTO((AccountTransaction) transaction))
                .collect(Collectors.toList());
    }

    private AccountTransactionDTO toDTO(AccountTransaction accountTransaction) {
        return AccountTransactionDTO.builder()
                .transactionType(accountTransaction.getTransactionType())
                .dateCreated(accountTransaction.getDateCreated())
                .recipientId(accountTransaction.getRecipientId())
                .senderId(accountTransaction.getSenderId())
                .currency(accountTransaction.getCurrency())
                .amount(accountTransaction.getAmount())
                .build();
    }
}
