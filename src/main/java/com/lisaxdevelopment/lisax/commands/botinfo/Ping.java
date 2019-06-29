package com.lisaxdevelopment.lisax.commands.botinfo;

import com.lisaxdevelopment.lisax.ChatListener;
import com.lisaxdevelopment.lisax.ExecutionInstance;
import com.lisaxdevelopment.lisax.commands.Command;
import com.lisaxdevelopment.lisax.commands.Flag;

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
