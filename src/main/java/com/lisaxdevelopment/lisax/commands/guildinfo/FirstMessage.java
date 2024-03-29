package com.lisaxdevelopment.lisax.commands.guildinfo;

import com.lisaxdevelopment.lisax.ChatListener;
import com.lisaxdevelopment.lisax.ExecutionInstance;
import com.lisaxdevelopment.lisax.commands.Command;
import com.lisaxdevelopment.lisax.commands.Flag;
import net.dv8tion.jda.api.entities.Message;

public class FirstMessage extends Command {

    public FirstMessage(ChatListener listener){
        super(listener);
        this.name = "firstMessage";
        this.description = "Get link and ID from the first message on the channel.";
        this.usage = listener.getPrefix() + "fm";
        this.aliases = new String[] { "firstM", "fMessage", "fm" };
        this.examples = listener.getPrefix() + "firstMessage";
        flags = new Flag[] {
                listener.getDeleteFlag(),
                listener.getIgnoreFlag()
        };
    }

    public void treat(ExecutionInstance instance){
        instance.getChannel().getHistoryAfter(0L, 1).queue( history -> {
            Message msg = history.getRetrievedHistory().get(0);
            sendMessage(instance, msg.getJumpUrl());
        });
    }
}
