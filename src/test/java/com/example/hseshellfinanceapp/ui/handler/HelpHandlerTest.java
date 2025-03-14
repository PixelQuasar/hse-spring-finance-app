package com.example.hseshellfinanceapp.ui.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HelpHandlerTest {

    private HelpHandler helpHandler;

    @BeforeEach
    void setUp() {
        helpHandler = new HelpHandler();
        helpHandler.init(); // Initialize menus
    }

    @Test
    void commands_withNoArgument_shouldReturnAllCommands() {
        // When
        String result = helpHandler.commands(null);

        // Then
        assertTrue(result.contains("FINANCE TRACKER - AVAILABLE COMMANDS"));
        assertFalse(result.contains("Account Management"));
        assertFalse(result.contains("Category Management"));
    }

    @Test
    void commands_withExistingCommand_shouldReturnCommandHelp() {
        // When
        String result = helpHandler.commands("create-account");

        // Then
        assertTrue(result.contains("Command: create-account"));
        assertTrue(result.contains("Description:"));
        assertTrue(result.contains("Usage:"));
    }

    @Test
    void welcome_shouldReturnWelcomeMessage() {
        // When
        String result = helpHandler.welcome();

        // Then
        assertTrue(result.contains("FINANCE TRACKER SHELL"));
        assertTrue(result.contains("Welcome"));
    }

    @Test
    void getMenus_shouldReturnAllConfiguredMenus() {
        // When
        int menuCount = helpHandler.getMenus().size();

        // Then
        assertTrue(menuCount >= 4); // At least the main menus
    }
}
