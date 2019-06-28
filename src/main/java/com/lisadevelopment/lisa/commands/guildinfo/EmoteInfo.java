package com.lisadevelopment.lisa.commands.guildinfo;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.ExecutionInstance;
import com.lisadevelopment.lisa.commands.Command;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class EmoteInfo extends Command {

    private static MessageChannel channel = null;

    EmoteInfo(ChatListener listener) {
        super(listener);
        this.name = "emoji";
        this.aliases = new String[]{"emote", "em"};
        this.description = "gets information about an emote";
        this.usage = listener.getPrefix() + "emote <:emote:>";
        this.examples = listener.getPrefix() + "emote :thinking:";
    }

    private static void reply(String message) {
        channel.sendMessage(message).queue();
    }

    @Override
    public void treat(ExecutionInstance instance) {
        channel = instance.getChannel();
        MessageReceivedEvent event = instance.getEvent();
        String str = instance.getArgs()[0];
        if (str.matches("<:.*:\\d+>")) {
            String id = str.replaceAll("<:.*:(\\d+)>", "$1");
            Emote emote = event.getJDA().getEmoteById(id);
            if (emote == null) {
                reply("Unknown emote:\n" +
                        "ID: `" + id + "`\n" +
                        "Guild: `Unknown`\n" +
                        "URL: https://discordcdn.com/emojis/" + id + ".png");
                return;
            }
            reply("Emote `" + emote.getName() + "`:\n" +
                    "ID: `" + emote.getId() + "`\n" +
                    "Guild: " + (emote.getGuild() == null ? "Unknown" : "`" + emote.getGuild().getName() + "`") + "\n" +
                    "URL: " + emote.getImageUrl());
            return;
        }
        if (str.codePoints().count() > 10) {
            reply("Error: Invalid emote/Input may be too long");
            return;
        }
        StringBuilder builder = new StringBuilder();
        str.codePoints().forEachOrdered(code -> {
            char[] chars = Character.toChars(code);
            String hex = Integer.toHexString(code).toUpperCase();
            while (hex.length() < 4)
                hex = "0" + hex;
            builder.append("`\\u").append(hex).append("`   ");
            if (chars.length > 1) {
                String hex0 = Integer.toHexString(chars[0]).toUpperCase();
                String hex1 = Integer.toHexString(chars[1]).toUpperCase();
                while (hex0.length() < 4)
                    hex0 = "0" + hex0;
                while (hex1.length() < 4)
                    hex1 = "0" + hex1;
                builder.append("[`\\u").append(hex0).append("\\u").append(hex1).append("`]   ");
            }
            builder.append(String.valueOf(chars)).append("   _").append(Character.getName(code)).append("_");
        });
        reply(builder.toString());
    }
}

