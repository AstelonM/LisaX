package com.lisaxdevelopment.lisax;

import com.lisaxdevelopment.lisax.commands.Flag;
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
        this.author = event.getAuthor();
        this.text = text;
        if (event.isFromGuild()) {
            textChannel = event.getTextChannel();
            guild = event.getGuild();
            member = event.getMember();
        } else {
            textChannel = null;
            guild = null;
            member = null;
        }
        shouldReply = true;
    }

    public String[] getArgs() {
        return text.substring(text.indexOf(" ")).trim().split(" ");
    }

    public MessageReceivedEvent getEvent() {
        return event;
    }

    public void setEvent(MessageReceivedEvent event) {
        this.event = event;
    }

    public Message getMessage() {
        return event.getMessage();
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public MessageChannel getChannel() {
        return channel;
    }

    public void setChannel(MessageChannel channel) {
        this.channel = channel;
    }

    public TextChannel getTextChannel() {
        return textChannel;
    }

    public void setTextChannel(TextChannel textChannel) {
        this.textChannel = textChannel;
    }

    public Guild getGuild() {
        return guild;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public HashSet<Flag> getFlags() {
        return flags;
    }

    public void addParam(Flag flag) {
        flags.add(flag);
    }

    public void setFlags(HashSet<Flag> flags) {
        this.flags = flags;
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

    public void setShouldReply(boolean shouldReply) {
        this.shouldReply = shouldReply;
    }
}
