package com.lisadevelopment.lisa.commands;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.time.Instant;

public class Flag {

    private ChatListener listener;
    private String name;
    private String description;
    private String[] aliases;

    public Flag(ChatListener listener, String name, String description, String[] aliases) {
        this.listener = listener;
        this.name = name;
        this.description = description;
        this.aliases = aliases;
    }

    public boolean isFlag(String text) {
        if (name.equalsIgnoreCase(text))
            return true;
        if (aliases != null) {
            int i;
            for (i = 0; i < aliases.length; i++)
                if (aliases[i].equalsIgnoreCase(text))
                    return true;
        }
        return false;
    }

    public MessageEmbed toHelpFormat() {
        EmbedBuilder result = new EmbedBuilder()
                .setAuthor("Flag info", null, listener.getJda().getSelfUser().getEffectiveAvatarUrl())
                .setColor(Config.BOT_COLOR)
                .setTimestamp(Instant.now());
        if (aliases == null || aliases.length == 0)
            result.addField("Name:", name, false);
        else
            result.addField("Name and aliases:", formatNamesToString(), false);
        result.addField("Description:", description, false);
        return result.build();
    }

    public String formatNamesToString() {
        StringBuilder result = new StringBuilder(name);
        int i;
        for (i = 0; i < aliases.length; i++)
            result.append(", ").append(aliases[i]);
        return result.toString();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String[] getAliases() {
        return aliases;
    }
}
