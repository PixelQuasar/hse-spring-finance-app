package com.example.hseshellfinanceapp.service.command.importCommands;

import java.io.InputStream;

import com.example.hseshellfinanceapp.service.io.template.AbstractDataImporter;
import com.example.hseshellfinanceapp.service.io.template.JsonDataImporter;

public class ImportJsonCommand extends ImportDataCommand {

    public ImportJsonCommand(JsonDataImporter dataImporter, InputStream inputStream) {
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
        return "IMPORT FROM JSON COMMAND\n" +
                "Imports financial data from a JSON file.\n" +
                "Usage: import-json --input <file-path>\n" +
                "Options:\n" +
                "  --input <file-path>: Path to the JSON file to import\n" +
                "Example:\n" +
                "  import-json --input finances.json\n\n" +
                "The JSON file should have the following structure:\n" +
                "{\n" +
                "  \"accounts\": [...],\n" +
                "  \"categories\": [...],\n" +
                "  \"operations\": [...]\n" +
                "}";
    }

    @Override
    public String getDescription() {
        return "Import financial data from JSON format";
    }
}
