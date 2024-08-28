package com.example.demo.dto;

import com.example.demo.enums.AccountType;

public class NotificationDTO {
    private AccountType accountType;
    private String identifier;

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}