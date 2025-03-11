package com.example.hseshellfinanceapp.service.command.categoryCommands;

import java.util.Optional;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.model.Category;
import com.example.hseshellfinanceapp.facade.CategoryFacade;
import com.example.hseshellfinanceapp.service.command.Command;

public class GetCategoryCommand extends Command<Optional<Category>> {

    private final CategoryFacade categoryFacade;
    private final UUID categoryId;

    public GetCategoryCommand(CategoryFacade categoryFacade, UUID categoryId) {
        this.categoryFacade = categoryFacade;
        this.categoryId = categoryId;
    }

    @Override
    public Optional<Category> execute() {
        return categoryFacade.getCategoryById(categoryId);
    }

    @Override
    protected boolean validate() {
        return categoryId != null;
    }

    @Override
    public String getHelp() {
        return "GET CATEGORY COMMAND\n" +
                "Retrieves details of a specific category.\n" +
                "Usage: get-category <category-id>\n" +
                "- category-id: UUID of the category to retrieve (required)";
    }

    @Override
    public String getDescription() {
        return "Get details for a specific category";
    }
}
