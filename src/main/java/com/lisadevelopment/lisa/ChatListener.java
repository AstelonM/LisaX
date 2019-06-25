package com.lisadevelopment.lisa;

import com.lisadevelopment.lisa.commands.Command;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class ChatListener implements EventListener, CommandHolder {

    private JDA jda;
    private String prefix = "//";
    private CommandHolder commands;

    public ChatListener(JDA jda) {
        this.jda = jda;
        commands = new CommandHolderImpl();
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

    @Override
    public ArrayList<Command> getCommands() {
        return commands.getCommands();
    }

    @Override
    public void addCommand(Command command) throws IllegalArgumentException {
        commands.addCommand(command);
    }

    @Override
    public Command findCommand(String name) {
        return commands.findCommand(name);
    }

    @Override
    public Command findFirstCommand(String name) {
        return commands.findFirstCommand(name);
    }

    @Override
    public int countDirectChildren() {
        return commands.countDirectChildren();
    }

    @Override
    public int countAllChildren() {
        return commands.countAllChildren();
    }

    @Override
    public ArrayList<MessageEmbed.Field> formatCommandList(String parentPrefix) {
        return commands.formatCommandList("");
    }
}
