package com.lisaxdevelopment.lisax.commands.botinfo;

import com.lisaxdevelopment.lisax.ChatListener;
import com.lisaxdevelopment.lisax.commands.CommandGroup;

public class BotInfoCommands extends CommandGroup {

    public BotInfoCommands(ChatListener listener) {
        super(listener);
        name = "BotInfoCommands";
        description = "Group of commands providing information about the bot.";
        addCommand(new CommandCount(listener));
        addCommand(new Help(listener));
        addCommand(new List(listener));
        addCommand(new Ping(listener));
    }
}
