package com.lisadevelopment.lisa.commands.botinfo;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.commands.CommandGroup;

public class BotInfoCommands extends CommandGroup {

    public BotInfoCommands(ChatListener listener) {
        super(listener);
        name = "BotInfoCommands";
        description = "Group of commands providing information about the bot.";
        addCommand(new Ping(listener));
    }
}
