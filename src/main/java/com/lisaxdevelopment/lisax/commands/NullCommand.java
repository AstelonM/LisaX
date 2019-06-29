package com.lisaxdevelopment.lisax.commands;

import com.lisaxdevelopment.lisax.ChatListener;
import com.lisaxdevelopment.lisax.ExecutionInstance;

public class NullCommand extends Command {

    public NullCommand(ChatListener listener) {
        super(listener);
        name = "-";
        description = "The null command, used as separator in appending commands, it does nothing otherwise.";
        usage = getPrefix() + "-";
    }

    @Override
    public void treat(ExecutionInstance instance) {}
}
