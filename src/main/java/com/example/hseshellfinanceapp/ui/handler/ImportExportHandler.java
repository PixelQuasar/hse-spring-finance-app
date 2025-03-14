package com.example.hseshellfinanceapp.ui.handler;

import com.example.hseshellfinanceapp.service.command.CommandExecutor;
import com.example.hseshellfinanceapp.service.command.exportCommands.FileExportCommand;
import com.example.hseshellfinanceapp.service.command.importCommands.FileImportCommand;
import com.example.hseshellfinanceapp.service.io.template.AbstractDataImporter;
import com.example.hseshellfinanceapp.service.io.template.JsonDataImporter;
import com.example.hseshellfinanceapp.service.io.template.TableDataImporter;
import com.example.hseshellfinanceapp.service.io.visitor.JsonExportVisitor;
import com.example.hseshellfinanceapp.service.io.visitor.TableExportVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class ImportExportHandler {
    
    private final CommandExecutor commandExecutor;
    private final JsonDataImporter jsonDataImporter;
    private final TableDataImporter tableDataImporter;
    private final JsonExportVisitor jsonExportVisitor;
    private final TableExportVisitor tableExportVisitor;

    @Autowired
    public ImportExportHandler(
            CommandExecutor commandExecutor,
            JsonDataImporter jsonDataImporter,
            TableDataImporter tableDataImporter,
            JsonExportVisitor jsonExportVisitor,
            TableExportVisitor tableExportVisitor) {
        this.commandExecutor = commandExecutor;
        this.jsonDataImporter = jsonDataImporter;
        this.tableDataImporter = tableDataImporter;
        this.jsonExportVisitor = jsonExportVisitor;
        this.tableExportVisitor = tableExportVisitor;
    }

    @ShellMethod(value = "Export data to JSON file", key = "export-json")
    public String exportJson(
            @ShellOption(help = "Output file path") String output,
            @ShellOption(help = "Include accounts", defaultValue = "true") boolean accounts,
            @ShellOption(help = "Include categories", defaultValue = "true") boolean categories,
            @ShellOption(help = "Include operations", defaultValue = "true") boolean operations) {
        try {
            FileExportCommand command = new FileExportCommand(
                    jsonExportVisitor, output, accounts, categories, operations);
            boolean success = commandExecutor.executeCommand(command);

            return success
                    ? "Data successfully exported to JSON file: " + output
                    : "Failed to export data to JSON";
        } catch (Exception e) {
            return "Error exporting data: " + e.getMessage();
        }
    }

    @ShellMethod(value = "Export data to table format", key = "export-table")
    public String exportTable(
            @ShellOption(help = "Output file path") String output,
            @ShellOption(help = "Include accounts", defaultValue = "true") boolean accounts,
            @ShellOption(help = "Include categories", defaultValue = "true") boolean categories,
            @ShellOption(help = "Include operations", defaultValue = "true") boolean operations) {
        try {
            FileExportCommand command = new FileExportCommand(
                    tableExportVisitor, output, accounts, categories, operations);
            boolean success = commandExecutor.executeCommand(command);

            return success
                    ? "Data successfully exported to table format: " + output
                    : "Failed to export data to table format";
        } catch (Exception e) {
            return "Error exporting data: " + e.getMessage();
        }
    }

    @ShellMethod(value = "Import data from JSON file", key = "import-json")
    public String importJson(@ShellOption(help = "Input file path") String input) {
        try {
            FileImportCommand command = new FileImportCommand(jsonDataImporter, input);
            AbstractDataImporter.ImportResult result = commandExecutor.executeCommand(command);

            return result.success()
                    ? "Data successfully imported from JSON file: " + result
                    : "Failed to import data: " + result.message();
        } catch (Exception e) {
            return "Error importing data: " + e.getMessage();
        }
    }

    @ShellMethod(value = "Import data from table format", key = "import-table")
    public String importTable(@ShellOption(help = "Input file path") String input) {
        try {
            FileImportCommand command = new FileImportCommand(tableDataImporter, input);
            AbstractDataImporter.ImportResult result = commandExecutor.executeCommand(command);

            return result.success()
                    ? "Data successfully imported from table format: " + result
                    : "Failed to import data: " + result.message();
        } catch (Exception e) {
            return "Error importing data: " + e.getMessage();
        }
    }
}
