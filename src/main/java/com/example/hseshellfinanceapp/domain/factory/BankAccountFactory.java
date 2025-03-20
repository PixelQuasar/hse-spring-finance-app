package com.example.hseshellfinanceapp.domain.factory;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.model.BankAccount;
import org.springframework.stereotype.Component;

@Component
public class BankAccountFactory {

    private static final String CARD_NUMBER_PATTERN = "^\\d{16}$";
    private static final String PHONE_PATTERN = "^\\+?[0-9]{10,15}$";

    public BankAccount createAccount(
            String name,
            BigDecimal initialBalance
    ) {
        validateName(name);
        validateBalance(initialBalance);

        return new BankAccount(
                generateId(),
                name,
                initialBalance
        );
    }

    public BankAccount createBasicAccount(String name) {
        return createAccount(
                name,
                BigDecimal.ZERO
        );
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("Name is too long");
        }
    }

    private void validateBalance(BigDecimal balance) {
        if (balance == null) {
            throw new IllegalArgumentException("The balance cannot be null");
        }
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Start balance cannot be negative");
        }
    }

    private UUID generateId() {
        return UUID.randomUUID();
    }
}
