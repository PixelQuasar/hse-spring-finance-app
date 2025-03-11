package com.example.hseshellfinanceapp.service.command.exportCommands;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.example.hseshellfinanceapp.service.command.Command;
import com.example.hseshellfinanceapp.service.io.visitor.AbstractExportVisitor;
import com.example.hseshellfinanceapp.service.io.visitor.JsonExportVisitor;
import com.example.hseshellfinanceapp.service.io.visitor.TableExportVisitor;

public class FileExportCommand extends Command<Boolean> {

    private final AbstractExportVisitor exportVisitor;
    private final String filePath;
    private final boolean includeAccounts;
    private final boolean includeCategories;
    private final boolean includeOperations;

    public FileExportCommand(
            AbstractExportVisitor exportVisitor,
            String filePath,
            boolean includeAccounts,
            boolean includeCategories,
            boolean includeOperations) {
        this.exportVisitor = exportVisitor;
        this.filePath = filePath;
        this.includeAccounts = includeAccounts;
        this.includeCategories = includeCategories;
        this.includeOperations = includeOperations;
    }

    @Override
    public Boolean execute() {
        try {
            // Create directory if it doesn't exist
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());

            // Create file output stream
            try (FileOutputStream outputStream = new FileOutputStream(new File(filePath))) {
                // Create appropriate export command
                ExportDataCommand exportCommand;

                if (exportVisitor instanceof JsonExportVisitor) {
                    exportCommand = new ExportJsonCommand(
                            (JsonExportVisitor) exportVisitor,
                            outputStream,
                            includeAccounts,
                            includeCategories,
                            includeOperations
                    );
                } else if (exportVisitor instanceof TableExportVisitor) {
                    exportCommand = new ExportTableCommand(
                            (TableExportVisitor) exportVisitor,
                            outputStream,
                            includeAccounts,
                            includeCategories,
                            includeOperations
                    );
                } else {
                    System.err.println("Unsupported export visitor type: " + exportVisitor.getClass().getName());
                    return false;
                }

                // Execute the export
                boolean result = exportCommand.execute();

                if (result) {
                    System.out.println("Data successfully exported to: " + filePath);
                }

                return result;
            }
        } catch (IOException e) {
            System.err.println("Error exporting data: " + e.getMessage());
            return false;
        }
    }

    @Override
    protected boolean validate() {
        return exportVisitor != null &&
                filePath != null && !filePath.trim().isEmpty() &&
                (includeAccounts || includeCategories || includeOperations);
    }

    @Override
    public String getHelp() {
        return "FILE EXPORT COMMAND\n" +
                "Exports financial data to a file in the specified format.\n" +
                "Usage: export-file --format <format> --output <file-path> [options]\n" +
                "Options:\n" +
                "  --format <format>: Export format (json, table)\n" +
                "  --output <file-path>: Path to save the exported file\n" +
                "  --accounts: Include accounts in export (default: true)\n" +
                "  --categories: Include categories in export (default: true)\n" +
                "  --operations: Include operations in export (default: true)\n" +
                "Example:\n" +
                "  export-file --format json --output ./exports/finances.json --accounts --operations";
    }

    @Override
    public String getDescription() {
        return "Export financial data to a file";
    }
}
