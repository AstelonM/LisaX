package com.lisadevelopment.lisa.commands;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.Config;
import com.lisadevelopment.lisa.ExecutionInstance;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.Instant;

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

    public MessageEmbed toHelpFormat(String commandName) {
        EmbedBuilder result = new EmbedBuilder()
                .setAuthor("Command info", null, listener.getJda().getSelfUser().getEffectiveAvatarUrl())
                .setColor(Config.BOT_COLOR)
                .setTimestamp(Instant.now());
        if (aliases == null || aliases.length == 0)
            result.addField("Name:", name, false);
        else
            result.addField("Name and aliases:", formatNamesToString(), false);
        result.addField("Description:", description, false)
                .addField("Usage:", usage, false);
        if (flags != null && flags.length > 0)
            result.addField("Flags:", formatFlagsToString(), false);
        return result.build();
    }

    public String formatNamesToString() {
        StringBuilder result = new StringBuilder(name);
        int i;
        for (i = 0; i < aliases.length; i++)
            result.append(", ").append(aliases[i]);
        return result.toString();
    }

    public String formatFlagsToString() {
        StringBuilder result = new StringBuilder(flags[0].getName());
        int i;
        for (i = 1; i < flags.length; i++)
            result.append(", ").append(flags[i].getName());
        return result.toString();
    }

    public String getPrefix() {
        return listener.getPrefix();
    }

    public ChatListener getListener() {
        return listener;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return usage;
    }

    public String[] getAliases() {
        return aliases;
    }

    public String getExamples() {
        return examples;
    }

    public Flag[] getFlags() {
        return flags;
    }
}
