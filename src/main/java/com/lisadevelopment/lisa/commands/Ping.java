package com.lisadevelopment.lisa.commands;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.ExecutionInstance;

public class Ping extends Command {

    public Ping(ChatListener listener) {
        super(listener);
        name = "ping";
        description = "Get the ping of the bot.";
        usage = getPrefix() + "ping";
        aliases = new String[] { "pong" };
        examples = getPrefix() + "ping";
    }

    @Override
    public void treat(ExecutionInstance instance) {
        listener.getJda().getRestPing().queue(ping ->
                instance.getChannel().sendMessage("The ping is " + ping + " ms!").queue());
    }
}
