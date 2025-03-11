package com.example.hseshellfinanceapp.service.command.category;

import java.util.List;

import com.example.hseshellfinanceapp.domain.model.Category;
import com.example.hseshellfinanceapp.domain.model.OperationType;
import com.example.hseshellfinanceapp.facade.CategoryFacade;
import com.example.hseshellfinanceapp.service.command.Command;

public class ListCategoriesCommand extends Command<List<Category>> {

    private final CategoryFacade categoryFacade;
    private final OperationType typeFilter;

    public ListCategoriesCommand(CategoryFacade categoryFacade) {
        this(categoryFacade, null);
    }

    public ListCategoriesCommand(CategoryFacade categoryFacade, OperationType typeFilter) {
        this.categoryFacade = categoryFacade;
        this.typeFilter = typeFilter;
    }

    @Override
    public List<Category> execute() {
        if (typeFilter == null) {
            return categoryFacade.getAllCategories();
        } else if (typeFilter == OperationType.INCOME) {
            return categoryFacade.getAllIncomeCategories();
        } else {
            return categoryFacade.getAllExpenseCategories();
        }
    }

    @Override
    public String getHelp() {
        return "LIST CATEGORIES COMMAND\n" +
                "Lists categories, optionally filtered by type.\n" +
                "Usage: list-categories [type]\n" +
                "- type: Optional filter - INCOME or EXPENSE\n" +
                "If no type is specified, all categories are listed.";
    }

    @Override
    public String getDescription() {
        return "List categories";
    }
}
