package com.example.hseshellfinanceapp.service.command.importCommands;

import java.io.InputStream;

import com.example.hseshellfinanceapp.service.io.template.AbstractDataImporter;
import com.example.hseshellfinanceapp.service.io.template.TableDataImporter;

public class ImportTableCommand extends ImportDataCommand {

    public ImportTableCommand(TableDataImporter dataImporter, InputStream inputStream) {
        super(dataImporter, inputStream);
    }

    @Override
    public AbstractDataImporter.ImportResult execute() {
        AbstractDataImporter.ImportResult result = super.execute();

        // Print summary of imported data
        System.out.println(result);

        return result;
    }

    @Override
    public String getHelp() {
        return "IMPORT FROM TABLE COMMAND\n" +
                "Imports financial data from a table format file (readable text tables).\n" +
                "Usage: import-table --input <file-path>\n" +
                "Options:\n" +
                "  --input <file-path>: Path to the text file to import\n" +
                "Example:\n" +
                "  import-table --input finances.txt\n\n" +
                "The table file should have sections marked with == ACCOUNTS ==, == CATEGORIES ==, etc.\n" +
                "Each section should have a header row and data rows in table format with | separators.";
    }

    @Override
    public String getDescription() {
        return "Import financial data from table format";
    }
}
