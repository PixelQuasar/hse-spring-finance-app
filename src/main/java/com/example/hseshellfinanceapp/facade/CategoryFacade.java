package com.example.hseshellfinanceapp.facade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.factory.CategoryFactory;
import com.example.hseshellfinanceapp.domain.model.Category;
import com.example.hseshellfinanceapp.domain.model.OperationType;
import com.example.hseshellfinanceapp.repository.CategoryRepository;
import com.example.hseshellfinanceapp.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CategoryFacade {

    private final CategoryRepository categoryRepository;
    private final OperationRepository operationRepository;
    private final CategoryFactory categoryFactory;

    @Autowired
    public CategoryFacade(
            CategoryRepository categoryRepository,
            OperationRepository operationRepository,
            CategoryFactory categoryFactory) {
        this.categoryRepository = categoryRepository;
        this.operationRepository = operationRepository;
        this.categoryFactory = categoryFactory;
    }

    public Category createCategory(String name, OperationType type) {
        Category category = categoryFactory.createCategory(name, type);
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getAllIncomeCategories() {
        return categoryRepository.findAllIncomeCategories();
    }

    public List<Category> getAllExpenseCategories() {
        return categoryRepository.findAllExpenseCategories();
    }

    public Optional<Category> getCategoryById(UUID id) {
        return categoryRepository.findById(id);
    }

    public Optional<Category> updateCategoryName(UUID id, String newName) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isEmpty()) {
            return Optional.empty();
        }

        Category category = categoryOpt.get();
        category.setName(newName);
        return Optional.of(categoryRepository.save(category));
    }

    @Transactional
    public boolean deleteCategory(UUID id) {
        if (operationRepository.findByCategoryId(id).size() > 0) {
            return false;
        }

        return categoryRepository.deleteById(id);
    }

    public Map<String, BigDecimal> getSpendingByCategory() {
        List<Category> expenseCategories = categoryRepository.findAllExpenseCategories();

        Map<String, BigDecimal> result = new HashMap<>();

        for (Category category : expenseCategories) {
            BigDecimal total = operationRepository.sumByCategoryId(category.getId());
            if (total != null && total.compareTo(BigDecimal.ZERO) > 0) {
                result.put(category.getName(), total);
            }
        }

        return result;
    }

    public Map<String, BigDecimal> getIncomeByCategory() {
        List<Category> incomeCategories = categoryRepository.findAllIncomeCategories();

        Map<String, BigDecimal> result = new HashMap<>();

        for (Category category : incomeCategories) {
            BigDecimal total = operationRepository.sumByCategoryId(category.getId());
            if (total != null && total.compareTo(BigDecimal.ZERO) > 0) {
                result.put(category.getName(), total);
            }
        }

        return result;
    }

    public List<CategorySummary> getCategoriesSortedByAmount(OperationType type) {
        List<Category> categories = categoryRepository.findByType(type);

        List<CategorySummary> summaries = new ArrayList<>();

        for (Category category : categories) {
            BigDecimal total = operationRepository.sumByCategoryId(category.getId());
            if (total == null) {
                total = BigDecimal.ZERO;
            }

            summaries.add(new CategorySummary(category, total));
        }

        summaries.sort((a, b) -> b.getAmount().compareTo(a.getAmount()));

        return summaries;
    }

    public static class CategorySummary {
        private final Category category;
        private final BigDecimal amount;

        public CategorySummary(Category category, BigDecimal amount) {
            this.category = category;
            this.amount = amount;
        }

        public Category getCategory() {
            return category;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        @Override
        public String toString() {
            return String.format("%s: $%.2f", category.getName(), amount);
        }
    }
}
