package com.example.hseshellfinanceapp.ui.handler;

import com.example.hseshellfinanceapp.service.command.CommandExecutor;
import com.example.hseshellfinanceapp.service.command.exportCommands.FileExportCommand;
import com.example.hseshellfinanceapp.service.command.importCommands.FileImportCommand;
import com.example.hseshellfinanceapp.service.io.template.AbstractDataImporter;
import com.example.hseshellfinanceapp.service.io.template.JsonDataImporter;
import com.example.hseshellfinanceapp.service.io.template.TableDataImporter;
import com.example.hseshellfinanceapp.service.io.visitor.JsonExportVisitor;
import com.example.hseshellfinanceapp.service.io.visitor.TableExportVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImportExportHandlerTest {

    @Mock
    private CommandExecutor commandExecutor;
    @Mock
    private JsonDataImporter jsonDataImporter;
    @Mock
    private TableDataImporter tableDataImporter;
    @Mock
    private JsonExportVisitor jsonExportVisitor;
    @Mock
    private TableExportVisitor tableExportVisitor;

    private ImportExportHandler importExportHandler;

    @BeforeEach
    void setUp() {
        importExportHandler = new ImportExportHandler(
                commandExecutor, jsonDataImporter, tableDataImporter, jsonExportVisitor, tableExportVisitor);
    }

    @Test
    void exportJson_shouldReturnSuccessMessage() {
        // Given
        String output = "test.json";

        when(commandExecutor.executeCommand(any(FileExportCommand.class))).thenReturn(true);

        // When
        String result = importExportHandler.exportJson(output, true, true, true);

        // Then
        assertTrue(result.contains("Data successfully exported"));
    }

    @Test
    void importJson_shouldReturnSuccessMessage() {
        // Given
        String input = "test.json";
        AbstractDataImporter.ImportResult importResult =
                new AbstractDataImporter.ImportResult(true, "Success", 1, 2, 3);

        when(commandExecutor.executeCommand(any(FileImportCommand.class))).thenReturn(importResult);

        // When
        String result = importExportHandler.importJson(input);

        // Then
        assertTrue(result.contains("Data successfully imported"));
    }
}
