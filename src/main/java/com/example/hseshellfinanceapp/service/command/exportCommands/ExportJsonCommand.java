package com.example.hseshellfinanceapp.service.command.exportCommands;

import java.io.OutputStream;

import com.example.hseshellfinanceapp.service.io.visitor.JsonExportVisitor;

public class ExportJsonCommand extends ExportDataCommand {

    public ExportJsonCommand(
            JsonExportVisitor exportVisitor,
            OutputStream outputStream,
            boolean includeAccounts,
            boolean includeCategories,
            boolean includeOperations) {
        super(exportVisitor, outputStream, includeAccounts, includeCategories, includeOperations);
    }

    @Override
    public String getHelp() {
        return "EXPORT TO JSON COMMAND\n" +
                "Exports financial data to JSON format.\n" +
                "Usage: export-json --output <file-path> [options]\n" +
                "Options:\n" +
                "  --output <file-path>: Path to save the JSON file\n" +
                "  --accounts: Include accounts in export (default: true)\n" +
                "  --categories: Include categories in export (default: true)\n" +
                "  --operations: Include operations in export (default: true)\n" +
                "Example:\n" +
                "  export-json --output finances.json --accounts --operations";
    }

    @Override
    public String getDescription() {
        return "Export financial data to JSON format";
    }
}
