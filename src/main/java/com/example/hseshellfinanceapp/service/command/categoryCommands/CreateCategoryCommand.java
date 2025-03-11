package com.example.hseshellfinanceapp.service.command.categoryCommands;

import com.example.hseshellfinanceapp.domain.model.Category;
import com.example.hseshellfinanceapp.domain.model.OperationType;
import com.example.hseshellfinanceapp.facade.CategoryFacade;
import com.example.hseshellfinanceapp.service.command.Command;

public class CreateCategoryCommand extends Command<Category> {

    private final CategoryFacade categoryFacade;
    private final String name;
    private final OperationType type;

    public CreateCategoryCommand(CategoryFacade categoryFacade, String name, OperationType type) {
        this.categoryFacade = categoryFacade;
        this.name = name;
        this.type = type;
    }

    @Override
    public Category execute() {
        return categoryFacade.createCategory(name, type);
    }

    @Override
    protected boolean validate() {
        return name != null && !name.trim().isEmpty() && type != null;
    }

    @Override
    public String getHelp() {
        return "CREATE CATEGORY COMMAND\n" +
                "Creates a new category for income or expense.\n" +
                "Usage: create-category <name> <type>\n" +
                "- name: Category name (required)\n" +
                "- type: Category type - INCOME or EXPENSE (required)";
    }

    @Override
    public String getDescription() {
        return "Create a new category";
    }
}
