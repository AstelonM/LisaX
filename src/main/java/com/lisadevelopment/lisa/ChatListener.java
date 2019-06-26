package com.lisadevelopment.lisa;

import com.lisadevelopment.lisa.commands.Command;
import com.lisadevelopment.lisa.commands.Flag;
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
    private String flagSeparator = "-";
    private CommandHolder commands;

    private Flag silentParam = new Flag("silent",
            "No response message will be sent.",
            new String[] { "s" }
    );
    private Flag deleteParam = new Flag("delete",
            "The command will be deleted if possible.",
            new String[] { "del", "d" }
    );
    private Flag chainingParam = new Flag("chaining",
            "Executes chained commands.",
            new String[] { "ch", "appending", "ap" }
    );
    private Flag chainedParam = new Flag("chained",
            "Marks that a command is chained.",
            new String[0]
    );
    private Flag ignoreParam = new Flag("ignore",
            "The command won't be executed (it will still count against limits).",
            new String[] { "ig" }
    );

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

    @Override
    public ArrayList<Command> getCommands() {
        return commands.getCommands();
    }

    public JDA getJda() {
        return jda;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getFlagSeparator() {
        return flagSeparator;
    }

    public Flag getSilentParam() {
        return silentParam;
    }

    public Flag getDeleteParam() {
        return deleteParam;
    }

    public Flag getChainingParam() {
        return chainingParam;
    }

    public Flag getChainedParam() {
        return chainedParam;
    }

    public Flag getIgnoreParam() {
        return ignoreParam;
    }
}
