package com.example.hseshellfinanceapp.service.command.exportCommands;

import java.io.OutputStream;

import com.example.hseshellfinanceapp.service.io.visitor.TableExportVisitor;

public class ExportTableCommand extends ExportDataCommand {

    public ExportTableCommand(
            TableExportVisitor exportVisitor,
            OutputStream outputStream,
            boolean includeAccounts,
            boolean includeCategories,
            boolean includeOperations) {
        super(exportVisitor, outputStream, includeAccounts, includeCategories, includeOperations);
    }

    @Override
    public String getHelp() {
        return "EXPORT TO TABLE COMMAND\n" +
                "Exports financial data to table format (readable text tables).\n" +
                "Usage: export-table --output <file-path> [options]\n" +
                "Options:\n" +
                "  --output <file-path>: Path to save the text file\n" +
                "  --accounts: Include accounts in export (default: true)\n" +
                "  --categories: Include categories in export (default: true)\n" +
                "  --operations: Include operations in export (default: true)\n" +
                "Example:\n" +
                "  export-table --output finances.txt --accounts --operations";
    }

    @Override
    public String getDescription() {
        return "Export financial data to table format";
    }
}
