package com.lisadevelopment.lisa;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;

import javax.annotation.Nonnull;

public class ChatListener implements EventListener {

    private JDA jda;
    private String prefix = "//";

    public ChatListener(JDA jda) {
        this.jda = jda;
    }

    @Override
    public void onEvent(@Nonnull GenericEvent event) {
        if (event instanceof MessageReceivedEvent)
            onMessageReceived((MessageReceivedEvent) event);
    }

    public void onMessageReceived(MessageReceivedEvent event) {
        User author = event.getAuthor();
        if (author.isBot() || event.isWebhookMessage())
            return;
    }

    public JDA getJda() {
        return jda;
    }

    public String getPrefix() {
        return prefix;
    }
}
