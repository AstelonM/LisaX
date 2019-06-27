package com.lisadevelopment.lisa.commands.guildinfo;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.ExecutionInstance;
import com.lisadevelopment.lisa.commands.Command;
import com.lisadevelopment.lisa.commands.Flag;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
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

    private MessageEmbed getGuildInfo(Member member) {
        User user = member.getUser();
        Guild guild = member.getGuild();
        String roleString = member.getRoles().stream().map(IMentionable::getAsMention)
                .collect(Collectors.joining(", "));
        String permissionString = member.getPermissions().stream().map(Permission::getName)
                .collect(Collectors.joining(", "));
        return new EmbedBuilder()
                .setAuthor("Guild Info", null, guild.getIconUrl())
                .setColor(Color.decode("random"))
                .setTimestamp(Instant.now())
                .setThumbnail(guild.getIconUrl())
                .setDescription(user.getAsMention() + " **(" + guild.getName() + "#" + user.getDiscriminator() + ")**")
                .addField("Name", guild.getName(), true)
                .addField("Server Region", guild.getRegion().toString(), true)
                .addField("Total members", String.valueOf(guild.getMembers().size()), true)
                .addField("Creation time",
                        guild.getTimeCreated().format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss")), true)
                .addField("Join time",
                        member.getTimeJoined().format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss")), true)
                .addField("Bot accounts", String.valueOf(guild.getMembers().stream().filter(mem -> mem.getUser().isBot()).count()), true)
                // .addField("Roles", String.join(',', guild.getRoles().stream().map(IMentionable::getAsMention).collect(Collectors.toCollection())), false)
                .addField("Permissions", permissionString, false)
                .build();
    }

    @Override
    public void treat(ExecutionInstance instance) { //TODO to implement

    }
}
