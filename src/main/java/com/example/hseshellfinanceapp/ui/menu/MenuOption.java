package com.example.hseshellfinanceapp.ui.menu;

public class MenuOption {
    private final String command;
    private final String description;
    private final String helpText;

    public MenuOption(String command, String description, String helpText) {
        this.command = command;
        this.description = description;
        this.helpText = helpText;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public String getHelpText() {
        return helpText;
    }

    @Override
    public String toString() {
        return command + " - " + description;
    }
}
