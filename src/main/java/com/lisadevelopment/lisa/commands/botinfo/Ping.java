package com.lisadevelopment.lisa.commands.botinfo;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.ExecutionInstance;
import com.lisadevelopment.lisa.commands.Command;
import com.lisadevelopment.lisa.commands.Flag;

public class Ping extends Command {

    public Ping(ChatListener listener) {
        super(listener);
        name = "ping";
        description = "Get the ping of the bot.";
        usage = getPrefix() + "ping";
        aliases = new String[] { "pong" };
        examples = getPrefix() + "ping";
        flags = new Flag[] {
                listener.getIgnoreFlag(),
                listener.getDeleteFlag()
        };
    }

    @Override
    public void treat(ExecutionInstance instance) {
        listener.getJda().getRestPing().queue(ping -> sendMessage(instance, "My ping is " + ping + " ms!"));
    }
}
