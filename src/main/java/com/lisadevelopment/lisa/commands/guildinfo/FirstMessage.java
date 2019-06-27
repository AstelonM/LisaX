package com.lisadevelopment.lisa.commands.guildinfo;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.ExecutionInstance;
import com.lisadevelopment.lisa.commands.Command;
import net.dv8tion.jda.api.entities.Message;

public class FirstMessage extends Command {
    private ExecutionInstance instance;
    public FirstMessage(ChatListener listener){
        super(listener);
        this.name = "firstMessage";
        this.description = "Get link and ID from the first message on the channel.";
        this.usage = listener.getPrefix() + "fm";
        this.aliases = new String[] { "firstM", "fMessage", "fm" };
        this.examples = listener.getPrefix() + "firstMessage";
    }

    public void treat(ExecutionInstance i){
        instance = i;
        instance.getChannel().getHistoryAfter(0L, 1).queue( history -> {
            Message msg = history.getRetrievedHistory().get(0);
            sendMessage(instance,msg.getJumpUrl());
        });
    }
}
