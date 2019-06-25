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

    public boolean isFlag(String text) {
        if (name.equalsIgnoreCase(text))
            return true;
        if (aliases != null) {
            int i;
            for (i = 0; i < aliases.length; i++)
                if (aliases[i].equalsIgnoreCase(text))
                    return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String[] getAliases() {
        return aliases;
    }
}
