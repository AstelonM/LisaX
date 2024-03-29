package com.lisaxdevelopment.lisax.utils;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class DiscordUtils {

    public static long parseSnowflake(String text) throws IllegalArgumentException {
        if (text == null || text.isEmpty())
            throw new IllegalArgumentException("The snowflake text cannot be empty");
        try {
            if (text.startsWith("-"))
                return Long.parseLong(text);
            else
                return Long.parseUnsignedLong(text);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("The format of the text isn't a proper long");
        }
    }

    public static void tryDelete(Message message) {
        if (message.isFromType(ChannelType.TEXT)) {
            TextChannel channel = message.getTextChannel();
            if (message.getGuild().getSelfMember().hasPermission(channel, Permission.MESSAGE_MANAGE))
                message.delete().queue();
        }
    }
}
