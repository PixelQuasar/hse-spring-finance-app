package com.example.hseshellfinanceapp.service.command.importCommands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.example.hseshellfinanceapp.service.command.Command;
import com.example.hseshellfinanceapp.service.io.template.AbstractDataImporter;
import com.example.hseshellfinanceapp.service.io.template.JsonDataImporter;
import com.example.hseshellfinanceapp.service.io.template.TableDataImporter;

public class FileImportCommand extends Command<AbstractDataImporter.ImportResult> {

    private final AbstractDataImporter dataImporter;
    private final String filePath;

    public FileImportCommand(AbstractDataImporter dataImporter, String filePath) {
        this.dataImporter = dataImporter;
        this.filePath = filePath;
    }

    @Override
    public AbstractDataImporter.ImportResult execute() {
        try {
            FileInputStream inputStream = new FileInputStream(new File(filePath));

            ImportDataCommand importCommand;

            if (dataImporter instanceof JsonDataImporter) {
                importCommand = new ImportJsonCommand(
                        (JsonDataImporter) dataImporter,
                        inputStream
                );
            } else if (dataImporter instanceof TableDataImporter) {
                importCommand = new ImportTableCommand(
                        (TableDataImporter) dataImporter,
                        inputStream
                );
            } else {
                System.err.println("Unsupported importer type: " + dataImporter.getClass().getName());
                return new AbstractDataImporter.ImportResult(
                        false,
                        "Unsupported importer type: " + dataImporter.getClass().getName(),
                        0, 0, 0
                );
            }

            AbstractDataImporter.ImportResult result = importCommand.execute();

            inputStream.close();

            return result;
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
            return new AbstractDataImporter.ImportResult(
                    false,
                    "File not found: " + filePath,
                    0, 0, 0
            );
        } catch (Exception e) {
            System.err.println("Error importing data: " + e.getMessage());
            return new AbstractDataImporter.ImportResult(
                    false,
                    "Error importing data: " + e.getMessage(),
                    0, 0, 0
            );
        }
    }

    @Override
    protected boolean validate() {
        return dataImporter != null && filePath != null && !filePath.trim().isEmpty();
    }

    @Override
    public String getHelp() {
        return "FILE IMPORT COMMAND\n" +
                "Imports financial data from a file in the specified format.\n" +
                "Usage: import-file --format <format> --input <file-path>\n" +
                "Options:\n" +
                "  --format <format>: Import format (json, table)\n" +
                "  --input <file-path>: Path to the file to import\n" +
                "Example:\n" +
                "  import-file --format json --input ./imports/finances.json";
    }

    @Override
    public String getDescription() {
        return "Import financial data from a file";
    }
}
