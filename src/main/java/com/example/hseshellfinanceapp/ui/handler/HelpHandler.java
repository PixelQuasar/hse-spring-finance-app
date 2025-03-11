package com.example.hseshellfinanceapp.ui.handler;

import java.util.ArrayList;
import java.util.List;

import com.example.hseshellfinanceapp.ui.menu.MenuOption;
import com.example.hseshellfinanceapp.ui.menu.ShellMenu;
import jakarta.annotation.PostConstruct;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class HelpHandler {

    private final List<ShellMenu> menus = new ArrayList<>();

    @PostConstruct
    public void init() {
        // Create accounts menu
        ShellMenu accountsMenu = new ShellMenu("Account Management");
        accountsMenu.addOption(new MenuOption("create-account", "Create a new bank account",
                "create-account <name> [initial-balance]"));
        accountsMenu.addOption(new MenuOption("list-accounts", "List all bank accounts",
                "list-accounts [min-balance]"));
        accountsMenu.addOption(new MenuOption("get-account", "Get account details",
                "get-account <account-id>"));
        accountsMenu.addOption(new MenuOption("update-account", "Update account name",
                "update-account <account-id> <new-name>"));
        accountsMenu.addOption(new MenuOption("delete-account", "Delete an account",
                "delete-account <account-id>"));
        accountsMenu.addOption(new MenuOption("total-balance", "Get total balance across all accounts",
                "total-balance"));
        menus.add(accountsMenu);

        // Create categories menu
        ShellMenu categoriesMenu = new ShellMenu("Category Management");
        categoriesMenu.addOption(new MenuOption("create-category", "Create a new category",
                "create-category <name> <type>"));
        categoriesMenu.addOption(new MenuOption("list-categories", "List categories",
                "list-categories [type]"));
        categoriesMenu.addOption(new MenuOption("get-category", "Get category details",
                "get-category <category-id>"));
        categoriesMenu.addOption(new MenuOption("update-category", "Update category name",
                "update-category <category-id> <new-name>"));
        categoriesMenu.addOption(new MenuOption("delete-category", "Delete a category",
                "delete-category <category-id>"));
        menus.add(categoriesMenu);

        // Create operations menu
        ShellMenu operationsMenu = new ShellMenu("Operation Management");
        operationsMenu.addOption(new MenuOption("add-income", "Create a new income operation",
                "add-income <account-id> <category-id> <amount> [description]"));
        operationsMenu.addOption(new MenuOption("add-expense", "Create a new expense operation",
                "add-expense <account-id> <category-id> <amount> [description]"));
        operationsMenu.addOption(new MenuOption("list-operations", "List operations",
                "list-operations [--account <id>] [--type <type>] [--from <date>] [--to <date>]"));
        operationsMenu.addOption(new MenuOption("get-operation", "Get operation details",
                "get-operation <operation-id>"));
        operationsMenu.addOption(new MenuOption("delete-operation", "Delete an operation",
                "delete-operation <operation-id>"));
        operationsMenu.addOption(new MenuOption("balance", "Get income/expense balance for a period",
                "balance <from-date> <to-date>"));
        menus.add(operationsMenu);

        // Create analytics menu
        ShellMenu analyticsMenu = new ShellMenu("Analytics");
        analyticsMenu.addOption(new MenuOption("expenses-by-category", "Get expenses by category",
                "expenses-by-category [month] [year]"));
        analyticsMenu.addOption(new MenuOption("income-by-category", "Get income by category",
                "income-by-category [month] [year]"));
        analyticsMenu.addOption(new MenuOption("account-summary", "Get account balance summary",
                "account-summary <account-id> <from-date> <to-date>"));
        menus.add(analyticsMenu);

        // Create import/export menu
        ShellMenu importExportMenu = new ShellMenu("Import/Export");
        importExportMenu.addOption(new MenuOption("export-json", "Export data to JSON file",
                "export-json <output> [--accounts] [--categories] [--operations]"));
        importExportMenu.addOption(new MenuOption("export-table", "Export data to table format",
                "export-table <output> [--accounts] [--categories] [--operations]"));
        importExportMenu.addOption(new MenuOption("import-json", "Import data from JSON file",
                "import-json <input>"));
        importExportMenu.addOption(new MenuOption("import-table", "Import data from table format",
                "import-table <input>"));
        menus.add(importExportMenu);
    }

    @ShellMethod(value = "Display available commands", key = {"help", "h", "?"})
    public String help(@ShellOption(defaultValue = ShellOption.NULL) String command) {
        if (command != null) {
            // Display help for specific command
            for (ShellMenu menu : menus) {
                MenuOption option = menu.findOptionByCommand(command);
                if (option != null) {
                    return String.format("""
                                    Command: %s
                                    Description: %s
                                    Usage: %s
                                    """,
                            option.getCommand(),
                            option.getDescription(),
                            option.getHelpText());
                }
            }
            return "Command not found: " + command;
        }

        // Display general help with all commands grouped by menu
        StringBuilder result = new StringBuilder();
        result.append("FINANCE TRACKER - AVAILABLE COMMANDS\n\n");

        for (ShellMenu menu : menus) {
            result.append(menu.render()).append("\n\n");
        }

        result.append("Type 'help <command>' for more information about a specific command.");
        return result.toString();
    }

    @ShellMethod(value = "Show welcome message", key = {"welcome", "w"})
    public String welcome() {
        return """
                ----------------------------------------
                |        FINANCE TRACKER SHELL         |
                ----------------------------------------
                
                Welcome to the Finance Tracker application!
                This shell allows you to manage your finances,
                track income and expenses, analyze your spending,
                and more.
                
                Type 'help' to see available commands.
                Type 'exit' to quit the application.
                
                ----------------------------------------
                """;
    }
}
