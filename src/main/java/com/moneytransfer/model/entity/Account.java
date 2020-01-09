package com.moneytransfer.model.entity;

import com.moneytransfer.model.enums.Currency;
import com.moneytransfer.util.DateUtil;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("accounts")
public class Account extends Model {
    public Account() {}

    public Account(Long userId, BigDecimal balance, String currency, LocalDateTime dateUpdated) {
        set("user_id", userId, "balance", balance, "currency", currency, "date_updated", dateUpdated);
    }

    public Long getUserId() {
        return getLong("user_id");
    }

    public BigDecimal getBalance() {
        return getBigDecimal("balance");
    }

    public Currency getCurrency() {
        return Currency.valueOf(getString("currency"));
    }

    public LocalDateTime getDateUpdated() {
        return DateUtil.getDateUpdated(getString("date_updated"));
    }

    public LocalDateTime getDateCreated() {
        return DateUtil.getDateCreated(getString("date_created"));
    }
}
