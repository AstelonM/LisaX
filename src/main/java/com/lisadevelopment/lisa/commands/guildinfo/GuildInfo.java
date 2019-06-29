package com.lisadevelopment.lisa.commands.guildinfo;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.Config;
import com.lisadevelopment.lisa.ExecutionInstance;
import com.lisadevelopment.lisa.commands.Command;
import com.lisadevelopment.lisa.commands.Flag;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;

import java.util.stream.Collectors;

public class GuildInfo extends Command {
    GuildInfo(ChatListener listener) {
        super(listener);
        name = "guildInfo";
        description = "Get information about the guild.";
        usage = getPrefix() + "guildInfo";
        aliases = new String[] { "getGuildInfo", "guildInformation", "getGuildInformation", "serverInfo",
                "getServerInfo", "serverInformation", "getServerInformation" };
        examples = getPrefix() + "guildInfo";
        flags = new Flag[] {
                listener.getChainingFlag(),
                listener.getDeleteFlag(),
                listener.getIgnoreFlag()
        };
    }

    private MessageEmbed getGuildInfo(Guild guild) {
        String roleString = guild.getRoles().stream().map(IMentionable::getAsMention)
                .collect(Collectors.joining(", "));
        String owner;
        if (guild.getOwner() == null)
            owner = "No owner";
        else
            owner = guild.getOwner().getAsMention() + " (" + guild.getOwner().getUser().getName() + "#" +
                    guild.getOwner().getUser().getDiscriminator() + ")";
        long botCount = guild.getMembers().stream().filter(mem -> mem.getUser().isBot()).count();
        long humanCount = guild.getMemberCache().size() - botCount;
        return new EmbedBuilder()
                .setAuthor("Guild Info", null, guild.getIconUrl())
                .setColor(Config.BOT_COLOR)
                .setThumbnail(guild.getIconUrl())
                .addField("Name", guild.getName(), true)
                .addField("Id", guild.getId(), true)
                .addField("Owner", owner, true)
                .addField("Server Region", guild.getRegion().getName(), true)
                .addField("Categories", String.valueOf(guild.getCategoryCache().size()), true)
                .addField("Text channels", String.valueOf(guild.getTextChannelCache().size()), true)
                .addField("Voice channels", String.valueOf(guild.getVoiceChannelCache().size()), true)
                .addField("Total members", String.valueOf(guild.getMemberCache().size()), true)
                .addField("Human accounts", String.valueOf(humanCount), true)
                .addField("Bot accounts", String.valueOf(botCount), true)
                .addField("Roles (" + guild.getRoleCache().size() + ")", roleString, false)
                .setFooter("Created at:", null)
                .setTimestamp(guild.getTimeCreated())
                .build();
    }

    @Override
    public void treat(ExecutionInstance instance) {
        sendMessage(instance, getGuildInfo(instance.getGuild()));
    }
}
