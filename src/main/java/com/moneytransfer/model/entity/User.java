package com.moneytransfer.model.entity;

import com.moneytransfer.model.enums.Currency;
import com.moneytransfer.util.DateUtil;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

@Table("users")
public class User extends Model {
    public User() {}

    public User(String firstName, String lastName, String email, LocalDateTime dateUpdated) {
        set("first_name", firstName, "last_name", lastName, "email", email, "date_updated", dateUpdated);
    }

    public String getFirstName() {
        return getString("first_name");
    }

    public String getLastName() {
        return getString("last_name");
    }

    public String getEmail() {
        return getString("email");
    }

    public String getDateUpdated() {
        return DateUtil.getDateUpdated(getString("date_updated"));
    }

    public String getDateCreated() {
        return DateUtil.getDateCreated(getString("date_created"));
    }
}
