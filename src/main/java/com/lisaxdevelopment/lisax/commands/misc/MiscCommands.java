package com.lisaxdevelopment.lisax.commands.misc;

import com.lisaxdevelopment.lisax.ChatListener;
import com.lisaxdevelopment.lisax.commands.CommandGroup;

public class MiscCommands extends CommandGroup {
    public MiscCommands(ChatListener listener) {
        super(listener);
        name = "MiscCommands";
        description = "Group of commands providing miscellaneous utilities.";
        addCommand(new CharCount(listener));
        addCommand(new Namemc(listener));
        addCommand(new Currency(listener));
        addCommand(new EmbedDissect(listener));
        addCommand(new OCR(listener));
    }
}
