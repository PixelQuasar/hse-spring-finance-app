package com.example.hseshellfinanceapp.service.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommandExecutorTest {

    private CommandExecutor commandExecutor;

    @Mock
    private Command<String> mockCommand;

    @BeforeEach
    void setUp() {
        commandExecutor = new CommandExecutor();
    }

    @Test
    void executeCommand_shouldCallExecuteWithValidation() {
        // Given
        when(mockCommand.executeWithValidation()).thenReturn("expected result");

        // When
        String result = commandExecutor.executeCommand(mockCommand);

        // Then
        assertEquals("expected result", result);
        verify(mockCommand).executeWithValidation();
    }

    @Test
    void executeCommand_whenExceptionThrown_shouldPropagateException() {
        // Given
        when(mockCommand.executeWithValidation()).thenThrow(new IllegalArgumentException("Test error"));

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> commandExecutor.executeCommand(mockCommand)
        );

        assertEquals("Test error", exception.getMessage());
    }

    @Test
    void getCommandHelp_shouldCallGetHelp() {
        // Given
        when(mockCommand.getHelp()).thenReturn("command help");

        // When
        String result = commandExecutor.getCommandHelp(mockCommand);

        // Then
        assertEquals("command help", result);
        verify(mockCommand).getHelp();
    }
}
