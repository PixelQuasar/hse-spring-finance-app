package com.example.hseshellfinanceapp.domain.model;

import java.util.UUID;

public class Category {
    private final UUID id;
    private String name;
    private OperationType type;

    public Category(UUID id, String name, OperationType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OperationType getType() {
        return type;
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("Category: %s (%s)", name, type);
    }

    public String toDetailedString() {
        return String.format("ID: %s\nName: %s\nType: %s", id, name, type);
    }
}
