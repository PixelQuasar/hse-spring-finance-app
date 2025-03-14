package com.example.hseshellfinanceapp.service.command;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.example.hseshellfinanceapp.service.command.exportCommands.ExportJsonCommand;
import com.example.hseshellfinanceapp.service.command.exportCommands.ExportTableCommand;
import com.example.hseshellfinanceapp.service.command.importCommands.ImportJsonCommand;
import com.example.hseshellfinanceapp.service.command.importCommands.ImportTableCommand;
import com.example.hseshellfinanceapp.service.io.template.AbstractDataImporter;
import com.example.hseshellfinanceapp.service.io.template.JsonDataImporter;
import com.example.hseshellfinanceapp.service.io.template.TableDataImporter;
import com.example.hseshellfinanceapp.service.io.visitor.JsonExportVisitor;
import com.example.hseshellfinanceapp.service.io.visitor.TableExportVisitor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExportCommandsTest {

    @Mock
    private JsonExportVisitor jsonExportVisitor;
    @Mock
    private TableExportVisitor tableExportVisitor;
    @Mock
    private OutputStream outputStream;

    // ExportJsonCommand tests
    @Test
    void exportJsonCommand_shouldCallVisitor() throws Exception {
        // Given
        ExportJsonCommand command = new ExportJsonCommand(
                jsonExportVisitor, outputStream, true, true, true);

        doNothing().when(jsonExportVisitor).exportData(outputStream, true, true, true);

        // When
        Boolean result = command.execute();

        // Then
        assertTrue(result);
        verify(jsonExportVisitor).exportData(outputStream, true, true, true);
    }

    // ExportTableCommand tests
    @Test
    void exportTableCommand_shouldCallVisitor() throws Exception {
        // Given
        ExportTableCommand command = new ExportTableCommand(
                tableExportVisitor, outputStream, true, true, true);

        doNothing().when(tableExportVisitor).exportData(outputStream, true, true, true);

        // When
        Boolean result = command.execute();

        // Then
        assertTrue(result);
        verify(tableExportVisitor).exportData(outputStream, true, true, true);
    }

    // Test validation
    @Test
    void exportDataCommand_withNoDataSelected_shouldFailValidation() {
        // Given
        ExportJsonCommand command = new ExportJsonCommand(
                jsonExportVisitor, outputStream, false, false, false);

        // When
        boolean isValid = command.validate();

        // Then
        assertFalse(isValid);
    }
}

@ExtendWith(MockitoExtension.class)
class ImportCommandsTest {

    @Mock
    private JsonDataImporter jsonDataImporter;
    @Mock
    private TableDataImporter tableDataImporter;

    // ImportJsonCommand tests
    @Test
    void importJsonCommand_shouldCallImporter() {
        // Given
        InputStream inputStream = new ByteArrayInputStream("{}".getBytes());
        ImportJsonCommand command = new ImportJsonCommand(jsonDataImporter, inputStream);

        AbstractDataImporter.ImportResult expectedResult = new AbstractDataImporter.ImportResult(
                true, "Success", 1, 2, 3);

        when(jsonDataImporter.importData(inputStream)).thenReturn(expectedResult);

        // When
        AbstractDataImporter.ImportResult result = command.execute();

        // Then
        assertSame(expectedResult, result);
        verify(jsonDataImporter).importData(inputStream);
    }

    // ImportTableCommand tests
    @Test
    void importTableCommand_shouldCallImporter() {
        // Given
        InputStream inputStream = new ByteArrayInputStream("table data".getBytes());
        ImportTableCommand command = new ImportTableCommand(tableDataImporter, inputStream);

        AbstractDataImporter.ImportResult expectedResult = new AbstractDataImporter.ImportResult(
                true, "Success", 1, 2, 3);

        when(tableDataImporter.importData(inputStream)).thenReturn(expectedResult);
        
        // Then
        verify(tableDataImporter).importData(inputStream);
    }

    // Test validation
    @Test
    void importDataCommand_withNullInputStream_shouldFailValidation() {
        // Given
        ImportJsonCommand command = new ImportJsonCommand(jsonDataImporter, null);

        // When
        boolean isValid = command.validate();

        // Then
        assertFalse(isValid);
    }
}
