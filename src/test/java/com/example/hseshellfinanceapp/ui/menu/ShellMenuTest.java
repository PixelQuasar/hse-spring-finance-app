package com.example.hseshellfinanceapp.ui.menu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShellMenuTest {

    @Test
    void constructor_shouldSetTitle() {
        // Given
        String title = "Test Menu";

        // When
        ShellMenu menu = new ShellMenu(title);

        // Then
        assertEquals(title, menu.getTitle());
        assertTrue(menu.getOptions().isEmpty());
    }

    @Test
    void addOption_shouldAddToOptionsList() {
        // Given
        ShellMenu menu = new ShellMenu("Test Menu");
        MenuOption option = new MenuOption("test", "Test Option", "Help Text");

        // When
        menu.addOption(option);

        // Then
        assertEquals(1, menu.getOptions().size());
        assertSame(option, menu.getOptions().get(0));
    }

    @Test
    void findOptionByCommand_shouldReturnOption_whenCommandExists() {
        // Given
        ShellMenu menu = new ShellMenu("Test Menu");
        MenuOption option1 = new MenuOption("cmd1", "Option 1", "Help 1");
        MenuOption option2 = new MenuOption("cmd2", "Option 2", "Help 2");
        menu.addOption(option1);
        menu.addOption(option2);

        // When
        MenuOption found = menu.findOptionByCommand("cmd2");

        // Then
        assertSame(option2, found);
    }

    @Test
    void findOptionByCommand_shouldReturnNull_whenCommandDoesNotExist() {
        // Given
        ShellMenu menu = new ShellMenu("Test Menu");
        menu.addOption(new MenuOption("cmd1", "Option 1", "Help 1"));

        // When
        MenuOption found = menu.findOptionByCommand("unknown");

        // Then
        assertNull(found);
    }

    @Test
    void render_shouldCreateTableWithTitleAndOptions() {
        // Given
        ShellMenu menu = new ShellMenu("Test Menu");
        menu.addOption(new MenuOption("cmd1", "Option 1", "Help 1"));
        menu.addOption(new MenuOption("cmd2", "Option 2", "Help 2"));

        // When
        String result = menu.render();

        // Then
        assertTrue(result.startsWith("TEST MENU"));
        assertTrue(result.contains("Command"));
        assertTrue(result.contains("Description"));
        assertTrue(result.contains("cmd1"));
        assertTrue(result.contains("Option 1"));
        assertTrue(result.contains("cmd2"));
        assertTrue(result.contains("Option 2"));
    }
}
