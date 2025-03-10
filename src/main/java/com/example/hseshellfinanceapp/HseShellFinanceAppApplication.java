package com.example.hseshellfinanceapp;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.factory.BankAccountFactory;
import com.example.hseshellfinanceapp.domain.model.BankAccount;
import com.example.hseshellfinanceapp.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HseShellFinanceAppApplication {
    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private BankAccountFactory bankAccountFactory;

    public static void main(String[] args) {
        SpringApplication.run(HseShellFinanceAppApplication.class, args);
    }

    /**
     * CommandLineRunner for demonstrating repository operations
     * Will run after the application context is loaded but before the shell starts
     */
    @Bean
    public CommandLineRunner demoRepositoryOperations() {
        return args -> {
            System.out.println("\n=== Finance Tracker Application Started ===\n");

            // Find all accounts
            System.out.println("--- All Bank Accounts ---");
            List<BankAccount> allAccounts = bankAccountRepository.findAll();
            allAccounts.forEach(account ->
                    System.out.println(account.toString())
            );

            // Find account by ID (using first account from the list if available)
            if (!allAccounts.isEmpty()) {
                UUID accountId = allAccounts.get(0).getId();
                System.out.println("\n--- Finding Account by ID ---");
                bankAccountRepository.findById(accountId)
                        .ifPresentOrElse(
                                account -> System.out.println("Found: " + account.toDetailedString()),
                                () -> System.out.println("Account not found with ID: " + accountId)
                        );

                // Update account balance
                System.out.println("\n--- Updating Account Balance ---");
                BigDecimal depositAmount = new BigDecimal("100.00");
                bankAccountRepository.updateBalance(accountId, depositAmount)
                        .ifPresent(updatedAccount ->
                                System.out.println("Balance updated: " + updatedAccount.toString())
                        );
            }

            // Create a new account
            System.out.println("\n--- Creating New Account ---");
            BankAccount newAccount = bankAccountFactory.createAccount(
                    "Demo Account",
                    new BigDecimal("500.00")
            );

            bankAccountRepository.save(newAccount);
            System.out.println("New account created: " + newAccount.toString());

            // Calculate total balance
            System.out.println("\n--- Calculating Total Balance ---");
            BigDecimal totalBalance = bankAccountRepository.getTotalBalance();
            System.out.println("Total balance across all accounts: $" + totalBalance);

            // Delete the demo account
            System.out.println("\n--- Deleting Demo Account ---");
            boolean deleted = bankAccountRepository.deleteById(newAccount.getId());
            System.out.println("Account deleted: " + deleted);

            System.out.println("\n=== Repository Demo Completed ===");
        };
    }
}
