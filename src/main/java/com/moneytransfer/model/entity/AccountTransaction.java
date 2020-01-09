package com.moneytransfer.model.entity;

import com.moneytransfer.model.enums.Currency;
import com.moneytransfer.model.enums.TransactionType;
import com.moneytransfer.util.DateUtil;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("account_transactions")
public class AccountTransaction extends Model {
    public AccountTransaction() {}

    public AccountTransaction(Long senderId, Long recipientId, BigDecimal amount, String currency, String transactionType) {
        set("sender_id", senderId, "recipient_id", recipientId, "amount", amount, "currency", currency, "transaction_type", transactionType);
    }

    public Long getSenderId() {
        return getLong("sender_id");
    }

    public Long getRecipientId() {
        return getLong("recipient_id");
    }

    public BigDecimal getAmount() {
        return getBigDecimal("amount");
    }

    public Currency getCurrency() {
        return Currency.valueOf(getString("currency"));
    }

    public TransactionType getTransactionType() {
        return TransactionType.valueOf(getString("transaction_type"));
    }

    public LocalDateTime getDateCreated() {
        return DateUtil.getDateCreated(getString("date_created"));
    }
}
