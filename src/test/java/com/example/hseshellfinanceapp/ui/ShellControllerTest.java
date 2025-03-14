package com.example.hseshellfinanceapp.ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import com.example.hseshellfinanceapp.ui.handler.HelpHandler;
import org.jline.utils.AttributedString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.shell.jline.PromptProvider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ShellControllerTest {

    @Mock
    private HelpHandler helpHandler;

    @Mock
    private ContextRefreshedEvent event;

    @Test
    void promptProvider_shouldReturnNonNullProvider() {
        // Given
        ShellController controller = new ShellController(helpHandler);

        // When
        PromptProvider provider = controller.promptProvider();

        // Then
        assertNotNull(provider);
        AttributedString prompt = provider.getPrompt();
        assertEquals("\u001B[32mfinance-tracker:> \u001B[0m", prompt.toAnsi());
    }

    @Test
    void onApplicationEvent_shouldCallDisplayWelcomeMessage() {
        // Given
        ShellController controller = spy(new ShellController(helpHandler));

        // When
        controller.onApplicationEvent(event);

        // Then
        verify(controller).displayWelcomeMessage();
    }

    @Test
    void displayWelcomeMessage_shouldPrintBanner() {
        // Given
        ShellController controller = new ShellController(helpHandler);
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try {
            // When
            controller.displayWelcomeMessage();

            // Then
            String output = outContent.toString();
            assertFalse(output.contains("FinanceTracker"));
            assertFalse(output.contains("HSE Shell Finance Application"));
            assertFalse(output.contains("Type 'commands'"));
        } finally {
            // Restore System.out
            System.setOut(originalOut);
        }
    }
}
