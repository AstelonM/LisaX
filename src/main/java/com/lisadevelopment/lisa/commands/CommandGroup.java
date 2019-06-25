package com.lisadevelopment.lisa.commands;

import com.lisadevelopment.lisa.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.Instant;
import java.util.ArrayList;

public abstract class CommandGroup extends Command implements CommandHolder {

    private CommandHolder commands;

    public CommandGroup(ChatListener listener) {
        super(listener);
        commands = new CommandHolderImpl();
    }

    @Override
    public void execute(MessageReceivedEvent event, String command) {
        findCommand(command).execute(event, command);
    }

    @Override
    public boolean treatHeader(ExecutionInstance instance) {
        return findCommand(instance.getCommand()).treatHeader(instance);
    }

    @Override
    public void treat(ExecutionInstance instance) {
        findCommand(instance.getCommand()).treat(instance);
    }

    @Override
    public MessageEmbed toHelpFormat(String commandName) {
        if (isCommand(commandName)) {
            EmbedBuilder result = new EmbedBuilder()
                    .setAuthor("Command info", null, listener.getJda().getSelfUser().getEffectiveAvatarUrl())
                    .setColor(Config.BOT_COLOR)
                    .setTimestamp(Instant.now());
            if (aliases == null || aliases.length == 0)
                result.addField("Name:", name, false);
            else
                result.addField("Name and aliases:", formatNamesToString(), false);
            result.addField("Description:", description, false);
            result.addField("Commands:", formatCommandsToString(), false);
            return result.build();
        } else
            return findCommand(commandName).toHelpFormat(commandName);
    }

    @Override
    public ArrayList<Command> getCommands() {
        return commands.getCommands();
    }

    @Override
    public void addCommand(Command command) throws IllegalArgumentException {
        commands.addCommand(command);
    }

    @Override
    public Command findCommand(String name) {
        if (isCommand(name))
            return this;
        return commands.findCommand(name);
    }

    @Override
    public Command findFirstCommand(String name) {
        if (isCommand(name))
            return this;
        return commands.findFirstCommand(name);
    }

    @Override
    public int countDirectChildren() {
        return commands.countDirectChildren();
    }

    @Override
    public int countAllChildren() {
        return commands.countAllChildren();
    }

    @Override
    public ArrayList<MessageEmbed.Field> formatCommandList(String parentPrefix) {
        return commands.formatCommandList(parentPrefix);
    }

    public String formatCommandsToString() {
        ArrayList<Command> commandList = commands.getCommands();
        if (commandList.isEmpty())
            return "";
        StringBuilder result = new StringBuilder(commandList.get(0).getName());
        int i;
        for (i = 1; i < commandList.size(); i++)
            result.append(", ").append(commandList.get(i).getName());
        return result.toString();
    }

    public String formatGroupCommands() {
        StringBuilder result = new StringBuilder(getName());
        result.append(" has the following commands:\n");
        for (Command command: commands.getCommands())
            result.append(command.getName()).append("\n");
        return result.toString().trim();
    }

    public String formatGroupCommandsAsList() {
        StringBuilder result = new StringBuilder();
        for (Command command: commands.getCommands())
            result.append(command.getName()).append("\n");
        return result.toString().trim();
    }
}
