package com.example.hseshellfinanceapp.service.command.category;

import java.util.Optional;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.model.Category;
import com.example.hseshellfinanceapp.facade.CategoryFacade;
import com.example.hseshellfinanceapp.service.command.Command;

public class UpdateCategoryCommand extends Command<Optional<Category>> {

    private final CategoryFacade categoryFacade;
    private final UUID categoryId;
    private final String newName;

    public UpdateCategoryCommand(CategoryFacade categoryFacade, UUID categoryId, String newName) {
        this.categoryFacade = categoryFacade;
        this.categoryId = categoryId;
        this.newName = newName;
    }

    @Override
    public Optional<Category> execute() {
        return categoryFacade.updateCategoryName(categoryId, newName);
    }

    @Override
    protected boolean validate() {
        return categoryId != null && newName != null && !newName.trim().isEmpty();
    }

    @Override
    public String getHelp() {
        return "UPDATE CATEGORY COMMAND\n" +
                "Updates an existing category's name.\n" +
                "Usage: update-category <category-id> <new-name>\n" +
                "- category-id: UUID of the category to update (required)\n" +
                "- new-name: New name for the category (required)";
    }

    @Override
    public String getDescription() {
        return "Update an existing category";
    }
}
