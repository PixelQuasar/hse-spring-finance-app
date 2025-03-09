package com.example.hseshellfinanceapp.domain.factory;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.regex.Pattern;

import com.example.hseshellfinanceapp.domain.model.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BankAccountFactory {

    private static final String CARD_NUMBER_PATTERN = "^\\d{16}$";
    private static final String PHONE_PATTERN = "^\\+?[0-9]{10,15}$";

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public BankAccountFactory(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public BankAccount createAccount(
            String name,
            BigDecimal initialBalance,
            String password,
            String phoneNumber,
            String cardNumber) {

        validateName(name);
        validateBalance(initialBalance);
        validatePassword(password);
        validatePhoneNumber(phoneNumber);
        validateCardNumber(cardNumber);

        String passwordHash = passwordEncoder.encode(password);

        return new BankAccount(
                generateId(),
                name,
                initialBalance,
                passwordHash,
                phoneNumber,
                cardNumber
        );
    }

    public BankAccount createBasicAccount(String name, String password, String cardNumber) {
        return createAccount(
                name,
                BigDecimal.ZERO,
                password,
                null, // необязательный телефон
                cardNumber
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

    private void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password should be at least 6 characters long");
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
            if (!Pattern.matches(PHONE_PATTERN, phoneNumber)) {
                throw new IllegalArgumentException("Invalid phone number format");
            }
        }
    }

    private void validateCardNumber(String cardNumber) {
        if (cardNumber == null || !Pattern.matches(CARD_NUMBER_PATTERN, cardNumber)) {
            throw new IllegalArgumentException("Card number should be 16 digits");
        }
    }

    private UUID generateId() {
        return UUID.randomUUID();
    }
}
