package com.lisaxdevelopment.lisax;

import com.lisaxdevelopment.lisax.commands.Command;
import com.lisaxdevelopment.lisax.commands.CommandGroup;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.ArrayList;
import java.util.HashMap;

public class CommandHolderImpl implements CommandHolder {

    private ArrayList<Command> commands;
    private HashMap<String, Integer> commandMap;

    public CommandHolderImpl() {
        commands = new ArrayList<>();
        commandMap = new HashMap<>();
    }

    private String getConflictIfExists(Command command) {
        if (commandMap.containsKey(command.getName().toLowerCase()))
            return command.getName();
        String[] aliases = command.getAliases();
        int i;
        for (i = 0; i < aliases.length; i++) {
            if (commandMap.containsKey(aliases[i].toLowerCase()))
                return aliases[i];
        }
        if (command instanceof CommandGroup) {
            CommandGroup commandGroup = (CommandGroup) command;
            ArrayList<Command> commands = commandGroup.getCommands();
            String result;
            for (Command com: commands) {
                result = getConflictIfExists(com);
                if (result != null)
                    return result;
            }
        }
        return null;
    }

    private void addCommandNames(Command command, Integer index) {
        commandMap.put(command.getName().toLowerCase(), index);
        String[] aliases = command.getAliases();
        int i;
        for (i = 0; i < aliases.length; i++)
            commandMap.put(aliases[i].toLowerCase(), index);
        if (command instanceof CommandGroup) {
            CommandGroup commandGroup = (CommandGroup) command;
            ArrayList<Command> commands = commandGroup.getCommands();
            for (Command com: commands) {
                addCommandNames(com, index);
            }
        }
    }

    @Override
    public void addCommand(Command command) throws IllegalArgumentException {
        String result = getConflictIfExists(command);
        if (result != null)
            throw new IllegalArgumentException("There already is a command with the name " + result + ".");
        int index = commands.size();
        commands.add(command);
        addCommandNames(command, index);
    }
    @Override
    public ArrayList<Command> getCommands() {
        return commands;
    }

    @Override
    public Command findCommand(String name) {
        Integer index = commandMap.get(name.toLowerCase());
        if (index == null)
            return null;
        else {
            if (commands.get(index) instanceof CommandGroup)
                return ((CommandGroup) commands.get(index)).findCommand(name);
            return commands.get(index);
        }
    }

    @Override
    public Command findFirstCommand(String name) {
        Integer index = commandMap.get(name.toLowerCase());
        if (index == null)
            return null;
        else
            return commands.get(index);
    }

    @Override
    public int countDirectChildren() {
        return commands.size();
    }

    @Override
    public int countAllChildren() {
        int nr = 0;
        int i;
        for (i = 0; i < commands.size(); i++) {
            if (commands.get(i) instanceof CommandGroup)
                nr += ((CommandGroup) commands.get(i)).countAllChildren();
            else
                nr++;
        }
        return nr;
    }

    @Override
    public ArrayList<MessageEmbed.Field> formatCommandList(String parentPrefix) {
        ArrayList<MessageEmbed.Field> result = new ArrayList<>();
        StringBuilder fieldResult = new StringBuilder();
        for (Command command: commands) {
            if (command instanceof CommandGroup) {
                String nextPrefix;
                if (parentPrefix.isEmpty())
                    nextPrefix = command.getName();
                else
                    nextPrefix = parentPrefix + " - " + command.getName();
                result.addAll(((CommandGroup) command).formatCommandList(nextPrefix));
            } else {
                if (fieldResult.length() == 0)
                    fieldResult.append(command.getName());
                else
                    fieldResult.append(", ").append(command.getName());
            }
        }
        if (fieldResult.length() != 0)
            result.add(new MessageEmbed.Field(parentPrefix, fieldResult.toString(), false));
        return result;
    }
}
