package com.example.hseshellfinanceapp.service.command;

public abstract class Command<T> {
    public abstract T execute();

    public abstract String getHelp();

    public abstract String getDescription();

    protected boolean validate() {
        return true;
    }

    public T executeWithValidation() {
        if (!validate()) {
            throw new IllegalArgumentException("Command validation failed");
        }
        return execute();
    }
}
