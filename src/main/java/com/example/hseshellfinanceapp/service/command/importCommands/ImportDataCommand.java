package com.example.hseshellfinanceapp.service.command.importCommands;

import java.io.InputStream;

import com.example.hseshellfinanceapp.service.command.Command;
import com.example.hseshellfinanceapp.service.io.template.AbstractDataImporter;

public abstract class ImportDataCommand extends Command<AbstractDataImporter.ImportResult> {

    protected final AbstractDataImporter dataImporter;
    protected final InputStream inputStream;

    public ImportDataCommand(AbstractDataImporter dataImporter, InputStream inputStream) {
        this.dataImporter = dataImporter;
        this.inputStream = inputStream;
    }

    @Override
    public AbstractDataImporter.ImportResult execute() {
        return dataImporter.importData(inputStream);
    }

    @Override
    protected boolean validate() {
        return dataImporter != null && inputStream != null;
    }

    @Override
    public String getHelp() {
        return "IMPORT DATA COMMAND\n" +
                "Imports financial data from the specified format.\n" +
                "Usage: import-data --format <format> --input <file-path>\n" +
                "Options:\n" +
                "  --format <format>: Import format (json, table)\n" +
                "  --input <file-path>: Path to the file to import\n" +
                "Example:\n" +
                "  import-data --format json --input finances.json";
    }

    @Override
    public String getDescription() {
        return "Import financial data";
    }
}
