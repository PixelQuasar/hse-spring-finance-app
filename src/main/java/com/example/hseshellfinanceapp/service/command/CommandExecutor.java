package com.example.hseshellfinanceapp.service.command;

import org.springframework.stereotype.Component;

@Component
public class CommandExecutor {
    public <T> T executeCommand(Command<T> command) {
        return command.executeWithValidation();
    }

    public String getCommandHelp(Command<?> command) {
        return command.getHelp();
    }
}
