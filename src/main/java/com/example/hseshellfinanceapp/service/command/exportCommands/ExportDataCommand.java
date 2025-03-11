package com.example.hseshellfinanceapp.service.command.exportCommands;

import java.io.OutputStream;

import com.example.hseshellfinanceapp.service.command.Command;
import com.example.hseshellfinanceapp.service.io.visitor.AbstractExportVisitor;

public abstract class ExportDataCommand extends Command<Boolean> {

    protected final AbstractExportVisitor exportVisitor;
    protected final OutputStream outputStream;
    protected final boolean includeAccounts;
    protected final boolean includeCategories;
    protected final boolean includeOperations;

    public ExportDataCommand(
            AbstractExportVisitor exportVisitor,
            OutputStream outputStream,
            boolean includeAccounts,
            boolean includeCategories,
            boolean includeOperations) {
        this.exportVisitor = exportVisitor;
        this.outputStream = outputStream;
        this.includeAccounts = includeAccounts;
        this.includeCategories = includeCategories;
        this.includeOperations = includeOperations;
    }

    @Override
    public Boolean execute() {
        try {
            exportVisitor.exportData(
                    outputStream,
                    includeAccounts,
                    includeCategories,
                    includeOperations
            );
            return true;
        } catch (Exception e) {
            System.err.println("Export failed: " + e.getMessage());
            return false;
        }
    }

    @Override
    protected boolean validate() {
        return exportVisitor != null &&
                outputStream != null &&
                (includeAccounts || includeCategories || includeOperations);
    }

    @Override
    public String getHelp() {
        return "EXPORT DATA COMMAND\n" +
                "Exports financial data to the specified format.\n" +
                "Usage: export-data --format <format> --output <file-path> [options]\n" +
                "Options:\n" +
                "  --format <format>: Export format (json, table)\n" +
                "  --output <file-path>: Path to save the exported file\n" +
                "  --accounts: Include accounts in export (default: true)\n" +
                "  --categories: Include categories in export (default: true)\n" +
                "  --operations: Include operations in export (default: true)\n" +
                "Example:\n" +
                "  export-data --format json --output finances.json --accounts --operations";
    }

    @Override
    public String getDescription() {
        return "Export financial data";
    }
}
