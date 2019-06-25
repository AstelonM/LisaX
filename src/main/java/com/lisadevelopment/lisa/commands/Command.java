package com.lisadevelopment.lisa.commands;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.ExecutionInstance;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


public abstract class Command {

    protected ChatListener listener;
    protected String name;
    protected String description;
    protected String usage;
    protected String[] aliases;
    protected String examples;
    protected Flag[] flags;

    public Command(ChatListener listener) {
        this.listener = listener;
        name = "";
        description = "";
        usage = "";
        aliases = new String[0];
        examples = "";
        flags = new Flag[0];
    }

    public void execute(MessageReceivedEvent event, String command) {
        ExecutionInstance instance = new ExecutionInstance(event, command);
        if (treatHeader(instance))
            treat(instance);
    }

    public boolean treatHeader(ExecutionInstance instance) {
        return true;
    }

    public abstract void treat(ExecutionInstance instance);

    public boolean isCommand(String text) {
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
}
