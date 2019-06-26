package com.lisadevelopment.lisa.commands.botinfo;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.ExecutionInstance;
import com.lisadevelopment.lisa.commands.Command;
import com.lisadevelopment.lisa.commands.Flag;

public class CommandCount extends Command {

    public CommandCount(ChatListener listener) {
        super(listener);
        name = "commandCount";
        description = "Get the number of commands registered.";
        usage = getPrefix() + "commandCount";
        aliases = new String[] { "countCommands" };
        examples = getPrefix() + "commandCount";
        flags = new Flag[] {
                listener.getDeleteFlag(),
                listener.getIgnoreFlag()
        };
    }

    @Override
    public void treat(ExecutionInstance instance) {
        int nr = listener.countAllChildren();
        sendMessage(instance, "This listener has " + nr + " commands registered.");
        instance.setResult("" + nr);
    }
}
