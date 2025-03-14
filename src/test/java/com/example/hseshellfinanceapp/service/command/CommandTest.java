package com.example.hseshellfinanceapp.service.command;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommandTest {

    @Test
    void executeWithValidation_whenValidationPasses_shouldExecuteCommand() {
        // Given
        TestCommand command = new TestCommand(true, "test result");

        // When
        String result = command.executeWithValidation();

        // Then
        assertEquals("test result", result);
        assertTrue(command.validateCalled);
        assertTrue(command.executeCalled);
    }

    @Test
    void executeWithValidation_whenValidationFails_shouldThrowException() {
        // Given
        TestCommand command = new TestCommand(false, "should not return");

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> command.executeWithValidation()
        );

        assertEquals("Command validation failed", exception.getMessage());
        assertTrue(command.validateCalled);
        assertFalse(command.executeCalled);
    }

    @Test
    void validate_byDefault_shouldReturnTrue() {
        // Given
        DefaultValidationCommand command = new DefaultValidationCommand();

        // When
        boolean result = command.validate();

        // Then
        assertTrue(result);
    }

    // Test implementation of Command
    private static class TestCommand extends Command<String> {
        private final boolean validationResult;
        private final String executionResult;
        boolean validateCalled = false;
        boolean executeCalled = false;

        TestCommand(boolean validationResult, String executionResult) {
            this.validationResult = validationResult;
            this.executionResult = executionResult;
        }

        @Override
        public String execute() {
            executeCalled = true;
            return executionResult;
        }

        @Override
        protected boolean validate() {
            validateCalled = true;
            return validationResult;
        }

        @Override
        public String getHelp() {
            return "Test help";
        }

        @Override
        public String getDescription() {
            return "Test description";
        }
    }

    // Command that uses the default validate implementation
    private static class DefaultValidationCommand extends Command<String> {
        @Override
        public String execute() {
            return "default";
        }

        @Override
        public String getHelp() {
            return "Help";
        }

        @Override
        public String getDescription() {
            return "Description";
        }
    }
}
