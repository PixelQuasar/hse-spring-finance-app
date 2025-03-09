package com.example.hseshellfinanceapp.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.model.BankAccount;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.example.hseshellfinanceapp.jooq.Tables.BANK_ACCOUNT;

@Repository
public class BankAccountRepository {

    private final DSLContext dsl;

    @Autowired
    public BankAccountRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public Optional<BankAccount> findById(UUID id) {
        Record record = dsl.select()
                .from(BANK_ACCOUNT)
                .where(BANK_ACCOUNT.ID.eq(id))
                .fetchOne();

        return Optional.ofNullable(record).map(this::mapToBankAccount);
    }

    public Optional<BankAccount> findByName(String name) {
        Record record = dsl.select()
                .from(BANK_ACCOUNT)
                .where(BANK_ACCOUNT.FULLNAME.eq(name))
                .fetchOne();

        return Optional.ofNullable(record).map(this::mapToBankAccount);
    }

    public Optional<BankAccount> findByCardNumber(String cardNumber) {
        Record record = dsl.select()
                .from(BANK_ACCOUNT)
                .where(BANK_ACCOUNT.CARD_NUMBER.eq(cardNumber))
                .fetchOne();

        return Optional.ofNullable(record).map(this::mapToBankAccount);
    }

    public Optional<BankAccount> findByPhoneNumber(String phoneNumber) {
        Record record = dsl.select()
                .from(BANK_ACCOUNT)
                .where(BANK_ACCOUNT.PHONE_NUMBER.eq(phoneNumber))
                .fetchOne();

        return Optional.ofNullable(record).map(this::mapToBankAccount);
    }

    public List<BankAccount> findAll() {
        return dsl.select()
                .from(BANK_ACCOUNT)
                .fetch()
                .map(this::mapToBankAccount);
    }

    @Transactional
    public BankAccount save(BankAccount bankAccount) {
        boolean exists = dsl.fetchExists(
                dsl.selectFrom(BANK_ACCOUNT)
                        .where(BANK_ACCOUNT.ID.eq(bankAccount.getId()))
        );

        if (exists) {
            // Обновление существующего счета
            dsl.update(BANK_ACCOUNT)
                    .set(BANK_ACCOUNT.FULLNAME, bankAccount.getName())
                    .set(BANK_ACCOUNT.BALANCE, bankAccount.getBalance())
                    .set(BANK_ACCOUNT.PASSWORD_HASH, bankAccount.getPasswordHash())
                    .set(BANK_ACCOUNT.PHONE_NUMBER, bankAccount.getPhoneNumber())
                    .set(BANK_ACCOUNT.CARD_NUMBER, bankAccount.getCardNumber())
                    .where(BANK_ACCOUNT.ID.eq(bankAccount.getId()))
                    .execute();
        } else {
            // Создание нового счета
            dsl.insertInto(BANK_ACCOUNT)
                    .set(BANK_ACCOUNT.ID, bankAccount.getId())
                    .set(BANK_ACCOUNT.FULLNAME, bankAccount.getName())
                    .set(BANK_ACCOUNT.BALANCE, bankAccount.getBalance())
                    .set(BANK_ACCOUNT.PASSWORD_HASH, bankAccount.getPasswordHash())
                    .set(BANK_ACCOUNT.PHONE_NUMBER, bankAccount.getPhoneNumber())
                    .set(BANK_ACCOUNT.CARD_NUMBER, bankAccount.getCardNumber())
                    .execute();
        }

        return bankAccount;
    }

    /**
     * Удаляет счет по ID
     */
    @Transactional
    public boolean deleteById(UUID id) {
        int affected = dsl.deleteFrom(BANK_ACCOUNT)
                .where(BANK_ACCOUNT.ID.eq(id))
                .execute();
        return affected > 0;
    }

    @Transactional
    public Optional<BankAccount> updateBalance(UUID id, BigDecimal amount) {
        // Получаем текущий баланс
        Record record = dsl.select(BANK_ACCOUNT.BALANCE)
                .from(BANK_ACCOUNT)
                .where(BANK_ACCOUNT.ID.eq(id))
                .fetchOne();

        if (record == null) {
            return Optional.empty();
        }

        BigDecimal currentBalance = record.get(BANK_ACCOUNT.BALANCE);
        BigDecimal newBalance = currentBalance.add(amount);

        // Обновляем баланс
        dsl.update(BANK_ACCOUNT)
                .set(BANK_ACCOUNT.BALANCE, newBalance)
                .where(BANK_ACCOUNT.ID.eq(id))
                .execute();

        // Возвращаем обновленный счет
        return findById(id);
    }

    public boolean existsById(UUID id) {
        return dsl.fetchExists(
                dsl.selectFrom(BANK_ACCOUNT)
                        .where(BANK_ACCOUNT.ID.eq(id))
        );
    }

    public boolean existsByName(String name) {
        return dsl.fetchExists(
                dsl.selectFrom(BANK_ACCOUNT)
                        .where(BANK_ACCOUNT.FULLNAME.eq(name))
        );
    }

    public boolean existsByCardNumber(String cardNumber) {
        return dsl.fetchExists(
                dsl.selectFrom(BANK_ACCOUNT)
                        .where(BANK_ACCOUNT.CARD_NUMBER.eq(cardNumber))
        );
    }

    public BigDecimal getTotalBalance() {
        return dsl.select(BANK_ACCOUNT.BALANCE.sum())
                .from(BANK_ACCOUNT)
                .fetchOne(0, BigDecimal.class);
    }

    private BankAccount mapToBankAccount(Record record) {
        return new BankAccount(
                record.get(BANK_ACCOUNT.ID),
                record.get(BANK_ACCOUNT.FULLNAME),
                record.get(BANK_ACCOUNT.BALANCE),
                record.get(BANK_ACCOUNT.PASSWORD_HASH),
                record.get(BANK_ACCOUNT.PHONE_NUMBER),
                record.get(BANK_ACCOUNT.CARD_NUMBER)
        );
    }
}
