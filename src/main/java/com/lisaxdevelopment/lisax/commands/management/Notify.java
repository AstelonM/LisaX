package com.lisaxdevelopment.lisax.commands.management;

import com.lisaxdevelopment.lisax.ChatListener;
import com.lisaxdevelopment.lisax.ExecutionInstance;
import com.lisaxdevelopment.lisax.commands.Command;
import com.lisaxdevelopment.lisax.commands.Flag;
import com.lisaxdevelopment.lisax.utils.Format;
import com.lisaxdevelopment.lisax.utils.GetFromString;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;

public class Notify extends Command {

    public Notify(ChatListener listener) {
        super(listener);
        name = "notify";
        description = "Mention any role without allowing other members to mention it, with a given message.";
        usage = getPrefix() + "notify <role name/id> | (message)";
        examples = getPrefix() + "notify member Read the new rules";
        flags = new Flag[] {
                listener.getDeleteFlag(),
                listener.getIgnoreFlag(),
                listener.getChainingFlag()
        };
    }

    @Override
    public void treat(ExecutionInstance instance) {
        String text = instance.getText();
        Member author = instance.getMember();
        Guild guild = instance.getGuild();
        Member bot = guild.getSelfMember();
        if (!text.contains(" ")) {
            sendMessage(instance, author.getAsMention() + " you need to provide at least a role.");
            return;
        }
        if (!author.hasPermission(Permission.MANAGE_ROLES)) {
            sendMessage(instance, author.getAsMention() + " you don't have permission to manage roles.");
            return;
        } else if (!bot.hasPermission(Permission.MANAGE_ROLES)) {
            sendMessage(instance, author.getAsMention() + " I don't have permission to manage roles.");
            return;
        }
        text = text.substring(text.indexOf(" ")).trim();
        String[] components = text.split("\\|", 2);
        if (components.length == 2)
            text = components[1].trim();
        else
            text = "";
        Role role = GetFromString.getRole(guild, components[0].trim(), Format.NAME, Format.ID);
        if (role == null) {
            sendMessage(instance, author.getAsMention() + " I couldn't find any role.");
            return;
        }
        if (!author.canInteract(role)) {
            sendMessage(instance, author.getAsMention() + " you can't interact with " + role.getName() + ".");
            return;
        } else if (!bot.canInteract(role)) {
            sendMessage(instance, author.getAsMention() + " I can't interact with " + role.getName() + ".");
            return;
        }
        if (!role.isMentionable()) {
            String finalText = text;
            try {
                role.getManager().setMentionable(true).queue(ignore -> notify(instance, role, finalText, false));
            } catch (Exception e) {
                sendMessage(instance, author.getAsMention() + " I couldn't modify " + role.getName() + ".");
            }
        } else
            notify(instance, role, text, true);
    }

    private void notify(ExecutionInstance instance, Role role, String message, boolean wasMentionable) {
        if (message == null)
            message = "";
        int maxLength = Message.MAX_CONTENT_LENGTH - role.getAsMention().length() - 1;
        if (message.length() >= maxLength)
            sendMessage(instance, instance.getAuthor().getAsMention() + " the message needs to be smaller than " +
                    maxLength + " characters.");
        else {
            if (instance.isShouldReply()) {
                instance.getChannel().sendMessage(role.getAsMention() + "\n" + message).queue( m -> {
                    if (!wasMentionable) {
                        role.getManager().setMentionable(false).queue(
                                ignore -> {
                                },
                                exception -> sendMessage(instance, instance.getAuthor() + " I couldn't set the role back to not " +
                                        "mentionable, you might want to have a look >_>")
                        );
                    }
                });
            }
        }
    }
}
