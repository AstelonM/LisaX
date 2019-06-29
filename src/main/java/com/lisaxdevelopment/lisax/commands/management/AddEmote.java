package com.lisaxdevelopment.lisax.commands.management;

import com.lisaxdevelopment.lisax.ChatListener;
import com.lisaxdevelopment.lisax.ExecutionInstance;
import com.lisaxdevelopment.lisax.commands.Command;
import com.lisaxdevelopment.lisax.commands.Flag;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;

public class AddEmote extends Command {

    public AddEmote(ChatListener listener) {
        super(listener);
        name = "addEmote";
        description = "Add a custom emotes to the server.";
        usage = getPrefix() + "addEmote <name> <attachedFile>";
        aliases = new String[] { "addEmoji" };
        flags = new Flag[] {
                listener.getSilentFlag(),
                listener.getDeleteFlag(),
                listener.getChainingFlag(),
                listener.getIgnoreFlag()
        };
    }

    @Override
    public void treat(ExecutionInstance instance) {
        Guild guild = instance.getGuild();
        List<Message.Attachment> attachments = instance.getEvent().getMessage().getAttachments();
        Member author = instance.getMember();
        Member bot = guild.getSelfMember();
        String text = instance.getText();
        if (!author.hasPermission(Permission.MANAGE_EMOTES))
            sendMessage(instance, author.getAsMention() + " you aren't really allowed to add emotes, you know >_>");
        else if (!bot.hasPermission(Permission.MANAGE_EMOTES))
            sendMessage(instance, "I don't have permission to add emotes, " + author.getAsMention() + ".");
        else if (!text.contains(" "))
            sendMessage(instance, author.getAsMention() + " you need to provide a name for the emote.");
        else if (attachments.isEmpty())
            sendMessage(instance, author.getAsMention() + " you need to provide an image for the emote.");
        else if (!attachments.get(0).isImage()) {
            sendMessage(instance, author.getAsMention() + " you need to provide an image.");
        } else {
            String finalText = text.substring(text.indexOf(" ")).trim();
            try {
                attachments.get(0).retrieveAsIcon().exceptionally(exception -> {
                    sendMessage(instance, author.getAsMention() + " I couldn't get the image.");
                    return null;
                }).thenAccept(emote -> {
                    if (emote != null)
                        guild.createEmote(finalText, emote).queue(
                                (ignored) -> sendMessage(instance, author.getAsMention() + " emote added."),
                                (exception) -> sendMessage(instance, author.getAsMention() + " I couldn't add the emote, " +
                                        "make sure there are enough slots left and that the file is smaller than 256kb " +
                                        "(also make sure I have permissions).")
                    );
                });

            } catch (Exception e) {
                e.printStackTrace();
                sendMessage(instance, author.getAsMention() + " I couldn't add the emote");
            }
        }
    }
}
