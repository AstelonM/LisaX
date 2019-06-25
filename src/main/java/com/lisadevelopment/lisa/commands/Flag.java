package com.lisadevelopment.lisa.commands;

public abstract class Flag {

    protected String name;
    protected String description;
    protected String[] aliases;

    public Flag(String name, String description, String[] aliases) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
    }
}
