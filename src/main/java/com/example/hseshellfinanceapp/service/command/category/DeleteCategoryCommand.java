package com.example.hseshellfinanceapp.service.command.category;

import java.util.UUID;

import com.example.hseshellfinanceapp.facade.CategoryFacade;
import com.example.hseshellfinanceapp.service.command.Command;

public class DeleteCategoryCommand extends Command<Boolean> {

    private final CategoryFacade categoryFacade;
    private final UUID categoryId;

    public DeleteCategoryCommand(CategoryFacade categoryFacade, UUID categoryId) {
        this.categoryFacade = categoryFacade;
        this.categoryId = categoryId;
    }

    @Override
    public Boolean execute() {
        return categoryFacade.deleteCategory(categoryId);
    }

    @Override
    protected boolean validate() {
        return categoryId != null;
    }

    @Override
    public String getHelp() {
        return "DELETE CATEGORY COMMAND\n" +
                "Deletes a category if it has no associated operations.\n" +
                "Usage: delete-category <category-id>\n" +
                "- category-id: UUID of the category to delete (required)\n" +
                "Note: Categories with associated operations cannot be deleted.";
    }

    @Override
    public String getDescription() {
        return "Delete a category";
    }
}
