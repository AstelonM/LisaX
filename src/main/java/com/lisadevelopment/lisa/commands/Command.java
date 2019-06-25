package com.lisadevelopment.lisa.commands;

import com.lisadevelopment.lisa.ChatListener;

public abstract class Command {

    protected ChatListener listener;
    protected String name;
    protected String description;
    protected String usage;
    protected String[] aliases;
    protected String examples;

    public Command(ChatListener listener) {
        this.listener = listener;
        name = "";
        description = "";
        usage = "";
        aliases = new String[0];
        examples = "";
    }
}
