package com.lisaxdevelopment.lisax;

import com.lisaxdevelopment.lisax.commands.Command;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.ArrayList;

public interface CommandHolder {

    ArrayList<Command> getCommands();
    void addCommand(Command command) throws IllegalArgumentException;
    Command findCommand(String name);
    Command findFirstCommand(String name);
    int countDirectChildren();
    int countAllChildren();
    ArrayList<MessageEmbed.Field> formatCommandList(String parentPrefix);
}
