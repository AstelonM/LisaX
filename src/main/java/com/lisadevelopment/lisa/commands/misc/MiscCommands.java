package com.lisadevelopment.lisa.commands.misc;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.commands.CommandGroup;

public class MiscCommands extends CommandGroup {
    public MiscCommands(ChatListener listener) {
        super(listener);
        name = "MiscCommands";
        description = "Group of commands providing miscellaneous utilities.";
        addCommand(new Namemc(listener));
        addCommand(new Currency(listener));
    }
}
