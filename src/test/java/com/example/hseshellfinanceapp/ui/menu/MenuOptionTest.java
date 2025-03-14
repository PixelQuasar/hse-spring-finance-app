package com.example.hseshellfinanceapp.ui.menu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MenuOptionTest {

    @Test
    void constructor_shouldInitializeFields() {
        // Given
        String command = "test-command";
        String description = "Test description";
        String helpText = "Test help text";

        // When
        MenuOption option = new MenuOption(command, description, helpText);

        // Then
        assertEquals(command, option.getCommand());
        assertEquals(description, option.getDescription());
        assertEquals(helpText, option.getHelpText());
    }

    @Test
    void getters_shouldReturnCorrectValues() {
        // Given
        MenuOption option = new MenuOption("cmd", "desc", "help");

        // Then
        assertEquals("cmd", option.getCommand());
        assertEquals("desc", option.getDescription());
        assertEquals("help", option.getHelpText());
    }

    @Test
    void toString_shouldFormatCorrectly() {
        // Given
        MenuOption option = new MenuOption("cmd", "desc", "help");

        // When
        String result = option.toString();

        // Then
        assertEquals("cmd - desc", result);
    }
}
