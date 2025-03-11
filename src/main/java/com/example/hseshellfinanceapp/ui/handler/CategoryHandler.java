package com.example.hseshellfinanceapp.ui.handler;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.model.Category;
import com.example.hseshellfinanceapp.domain.model.OperationType;
import com.example.hseshellfinanceapp.facade.CategoryFacade;
import com.example.hseshellfinanceapp.service.command.CommandExecutor;
import com.example.hseshellfinanceapp.service.command.categoryCommands.CreateCategoryCommand;
import com.example.hseshellfinanceapp.service.command.categoryCommands.DeleteCategoryCommand;
import com.example.hseshellfinanceapp.service.command.categoryCommands.GetCategoryCommand;
import com.example.hseshellfinanceapp.service.command.categoryCommands.ListCategoriesCommand;
import com.example.hseshellfinanceapp.service.command.categoryCommands.UpdateCategoryCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;

@ShellComponent
public class CategoryHandler {

    private final CategoryFacade categoryFacade;
    private final CommandExecutor commandExecutor;

    @Autowired
    public CategoryHandler(CategoryFacade categoryFacade, CommandExecutor commandExecutor) {
        this.categoryFacade = categoryFacade;
        this.commandExecutor = commandExecutor;
    }

    @ShellMethod(value = "Create a new category", key = "create-category")
    public String createCategory(
            @ShellOption(help = "Category name") String name,
            @ShellOption(help = "Category type (INCOME or EXPENSE)") OperationType type) {
        try {
            CreateCategoryCommand command = new CreateCategoryCommand(categoryFacade, name, type);
            Category category = commandExecutor.executeCommand(command);
            return "Category created successfully: " + category.toString();
        } catch (Exception e) {
            return "Error creating category: " + e.getMessage();
        }
    }

    @ShellMethod(value = "List categories", key = "list-categories")
    public String listCategories(
            @ShellOption(help = "Filter by type (INCOME or EXPENSE)", defaultValue = ShellOption.NULL)
            OperationType typeFilter) {
        try {
            ListCategoriesCommand command = new ListCategoriesCommand(categoryFacade, typeFilter);
            List<Category> categories = commandExecutor.executeCommand(command);

            if (categories.isEmpty()) {
                return "No categories found.";
            }

            String[][] data = new String[categories.size() + 1][3];
            data[0] = new String[]{"ID", "Name", "Type"};

            for (int i = 0; i < categories.size(); i++) {
                Category category = categories.get(i);
                data[i + 1] = new String[]{
                        category.getId().toString(),
                        category.getName(),
                        category.getType().toString()
                };
            }

            ArrayTableModel model = new ArrayTableModel(data);
            TableBuilder tableBuilder = new TableBuilder(model);
            tableBuilder.addHeaderAndVerticalsBorders(BorderStyle.fancy_light);

            String title = typeFilter == null ? "All Categories" : typeFilter + " Categories";
            return title + ":\n" + tableBuilder.build().render(80);
        } catch (Exception e) {
            return "Error listing categories: " + e.getMessage();
        }
    }

    @ShellMethod(value = "Get category details", key = "get-category")
    public String getCategory(@ShellOption(help = "Category ID") UUID categoryId) {
        try {
            GetCategoryCommand command = new GetCategoryCommand(categoryFacade, categoryId);
            Optional<Category> categoryOpt = commandExecutor.executeCommand(command);

            return categoryOpt.map(category -> category.toDetailedString())
                    .orElse("Category not found with ID: " + categoryId);
        } catch (Exception e) {
            return "Error retrieving category: " + e.getMessage();
        }
    }

    @ShellMethod(value = "Update category name", key = "update-category")
    public String updateCategory(
            @ShellOption(help = "Category ID") UUID categoryId,
            @ShellOption(help = "New category name") String newName) {
        try {
            UpdateCategoryCommand command = new UpdateCategoryCommand(categoryFacade, categoryId, newName);
            Optional<Category> categoryOpt = commandExecutor.executeCommand(command);

            return categoryOpt.map(category -> "Category updated successfully: " + category.toString())
                    .orElse("Category not found with ID: " + categoryId);
        } catch (Exception e) {
            return "Error updating category: " + e.getMessage();
        }
    }

    @ShellMethod(value = "Delete a category", key = "delete-category")
    public String deleteCategory(@ShellOption(help = "Category ID") UUID categoryId) {
        try {
            DeleteCategoryCommand command = new DeleteCategoryCommand(categoryFacade, categoryId);
            boolean success = commandExecutor.executeCommand(command);

            return success
                    ? "Category deleted successfully"
                    : "Failed to delete category. It may have associated operations or might not exist.";
        } catch (Exception e) {
            return "Error deleting category: " + e.getMessage();
        }
    }
}
