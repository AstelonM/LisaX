package com.lisadevelopment.lisa;

import com.lisadevelopment.lisa.commands.Flag;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashSet;

public class ExecutionInstance {

    private MessageReceivedEvent event;
    private String command;
    private MessageChannel channel;
    private TextChannel textChannel;
    private Guild guild;
    private User author;
    private Member member;
    private String text;
    private HashSet<Flag> flags;
    private String result;
    private boolean shouldReply;

    public ExecutionInstance(MessageReceivedEvent event, String command) {
        init(event, command, event.getMessage().getContentRaw());
    }

    public ExecutionInstance(MessageReceivedEvent event, String command, String text) {
        init(event, command, text);
    }

    private void init(MessageReceivedEvent event, String command, String text) {
        flags = new HashSet<>();
        this.event = event;
        this.command = command;
        this.channel = event.getChannel();
        this.textChannel = event.getTextChannel();
        this.guild = event.getGuild();
        this.author = event.getAuthor();
        this.member = event.getMember();
        this.text = text;
        shouldReply = true;
    }

    public MessageReceivedEvent getEvent() {
        return event;
    }

    public String getCommand() {
        return command;
    }

    public MessageChannel getChannel() {
        return channel;
    }

    public TextChannel getTextChannel() {
        return textChannel;
    }

    public Guild getGuild() {
        return guild;
    }

    public User getAuthor() {
        return author;
    }

    public Member getMember() {
        return member;
    }

    public String getText() {
        return text;
    }

    public HashSet<Flag> getFlags() {
        return flags;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isShouldReply() {
        return shouldReply;
    }
}
