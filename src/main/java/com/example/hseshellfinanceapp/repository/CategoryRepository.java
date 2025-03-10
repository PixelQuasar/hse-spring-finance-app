package com.example.hseshellfinanceapp.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.model.Category;
import com.example.hseshellfinanceapp.domain.model.OperationType;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.example.hseshellfinanceapp.jooq.tables.Category.CATEGORY;

@Repository
public class CategoryRepository {

    private final DSLContext dsl;

    @Autowired
    public CategoryRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public Optional<Category> findById(UUID id) {
        Record record = dsl.select()
                .from(CATEGORY)
                .where(CATEGORY.ID.eq(id))
                .fetchOne();

        return Optional.ofNullable(record).map(this::mapToCategory);
    }

    public Optional<Category> findByName(String name) {
        Record record = dsl.select()
                .from(CATEGORY)
                .where(CATEGORY.NAME.eq(name))
                .fetchOne();

        return Optional.ofNullable(record).map(this::mapToCategory);
    }

    public List<Category> findAll() {
        return dsl.select()
                .from(CATEGORY)
                .fetch()
                .map(this::mapToCategory);
    }

    public List<Category> findByType(OperationType type) {
        return dsl.select()
                .from(CATEGORY)
                .where(CATEGORY.TYPE.eq(type.name()))
                .fetch()
                .map(this::mapToCategory);
    }

    public List<Category> findAllIncomeCategories() {
        return findByType(OperationType.INCOME);
    }

    public List<Category> findAllExpenseCategories() {
        return findByType(OperationType.EXPENSE);
    }

    @Transactional
    public Category save(Category category) {
        boolean exists = dsl.fetchExists(
                dsl.selectFrom(CATEGORY)
                        .where(CATEGORY.ID.eq(category.getId()))
        );

        if (exists) {
            // Update existing category
            dsl.update(CATEGORY)
                    .set(CATEGORY.NAME, category.getName())
                    .set(CATEGORY.TYPE, category.getType().name())
                    .where(CATEGORY.ID.eq(category.getId()))
                    .execute();
        } else {
            // Create new category
            dsl.insertInto(CATEGORY)
                    .set(CATEGORY.ID, category.getId())
                    .set(CATEGORY.NAME, category.getName())
                    .set(CATEGORY.TYPE, category.getType().name())
                    .execute();
        }

        return category;
    }

    @Transactional
    public boolean deleteById(UUID id) {
        int affected = dsl.deleteFrom(CATEGORY)
                .where(CATEGORY.ID.eq(id))
                .execute();
        return affected > 0;
    }

    public boolean existsById(UUID id) {
        return dsl.fetchExists(
                dsl.selectFrom(CATEGORY)
                        .where(CATEGORY.ID.eq(id))
        );
    }

    public boolean existsByName(String name) {
        return dsl.fetchExists(
                dsl.selectFrom(CATEGORY)
                        .where(CATEGORY.NAME.eq(name))
        );
    }

    public int countByType(OperationType type) {
        return dsl.fetchCount(
                dsl.selectFrom(CATEGORY)
                        .where(CATEGORY.TYPE.eq(type.name()))
        );
    }

    private Category mapToCategory(Record record) {
        return new Category(
                record.get(CATEGORY.ID),
                record.get(CATEGORY.NAME),
                OperationType.valueOf(record.get(CATEGORY.TYPE))
        );
    }
}
