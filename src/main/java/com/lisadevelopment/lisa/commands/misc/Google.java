package com.lisadevelopment.lisa.commands.misc;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.Config;
import com.lisadevelopment.lisa.ExecutionInstance;
import com.lisadevelopment.lisa.commands.Command;

import net.dv8tion.jda.api.EmbedBuilder;

import java.time.Instant;

public class Google extends Command {
    Google(ChatListener listener) {
        super(listener);
        name = "google";
        description = "Get first result of a Google search.";
        usage = listener.getPrefix() + "google <searchContent>";
        aliases = new String[]{"g"};
        examples = listener.getPrefix() + "namemc Astelon";
    }

    @Override
    public void treat(ExecutionInstance instance) {
        if (instance.getMessage().getContentRaw().split("\\s").length < 2) {
            sendMessage(instance, "https://www.google.com/");
            return;
        }
        sendEmbedMessage(instance);
    }

    private String getSearchContent(String[] args){
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for(String arg : args){
            if(i++ == 0) continue;
            sb.append(arg);
        }
        return sb.toString();
    }

    private void sendEmbedMessage(ExecutionInstance instance){
        sendMessage(instance, new EmbedBuilder()
                .setTitle("Google `" + getSearchContent(instance.getMessage().getContentRaw().split("\\s"))+"`")
                .setDescription("https://www.google.com/search?q="+getSearchContent(instance.getMessage().getContentRaw().split("\\s")))
                .setColor(Config.BOT_COLOR)
                .setTimestamp(Instant.now())
                .build()
        );
    }
}
