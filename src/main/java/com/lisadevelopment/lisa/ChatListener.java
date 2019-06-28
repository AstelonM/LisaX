package com.lisadevelopment.lisa;

import com.lisadevelopment.lisa.commands.Command;
import com.lisadevelopment.lisa.commands.CommandGroup;
import com.lisadevelopment.lisa.commands.Flag;
import com.lisadevelopment.lisa.commands.NullCommand;
import com.lisadevelopment.lisa.utils.StringUtils;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
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
    private MongoDatabase db;
    private String prefix = "//";
    private String flagSeparator = "-";
    private CommandHolder commands;

    private NullCommand nullCommand;

    private Flag silentFlag = new Flag("silent",
            "No response message will be sent.",
            new String[] { "s" }
    );
    private Flag deleteFlag = new Flag("delete",
            "The command will be deleted if possible.",
            new String[] { "del", "d" }
    );
    private Flag chainingFlag = new Flag("chaining",
            "Executes chained commands.",
            new String[] { "ch", "appending", "ap" }
    );
    private Flag chainedFlag = new Flag("chained",
            "Marks that a command is chained.",
            new String[0]
    );
    private Flag ignoreFlag = new Flag("ignore",
            "The command won't be executed (it will still count against limits).",
            new String[] { "ig" }
    );

    public ChatListener(JDA jda, MongoClient mongoClient) {
        this.jda = jda;
        this.db = mongoClient.getDatabase("lisa");
        commands = new CommandHolderImpl();
        nullCommand = new NullCommand(this);
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
        String text = event.getMessage().getContentRaw();
        if (!text.startsWith(prefix))
            return;
        text = text.substring(prefix.length());
        if (text.trim().isEmpty())
            return;
        text = StringUtils.firstWord(text);
        String commandName = StringUtils.firstWord(text, flagSeparator);
        Command command = findFirstCommand(commandName);
        if (command != null)
            command.execute(event, commandName);
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

    public CommandGroup findCommandGroup(String name) {
        Command command = findCommand(name);
        if (!(command instanceof CommandGroup))
            return null;
        else
            return (CommandGroup) command;
    }

    public JDA getJda() {
        return jda;
    }

    public MongoDatabase getDb() { return db; }

    public String getPrefix() {
        return prefix;
    }

    public String getFlagSeparator() {
        return flagSeparator;
    }

    public boolean isNullCommand(String text) {
        return nullCommand.isCommand(text);
    }

    public Flag getSilentFlag() {
        return silentFlag;
    }

    public Flag getDeleteFlag() {
        return deleteFlag;
    }

    public Flag getChainingFlag() {
        return chainingFlag;
    }

    public Flag getChainedFlag() {
        return chainedFlag;
    }

    public Flag getIgnoreFlag() {
        return ignoreFlag;
    }
}
