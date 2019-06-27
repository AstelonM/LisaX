package com.lisadevelopment.lisa.commands.guildinfo;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.ExecutionInstance;
import com.lisadevelopment.lisa.commands.Command;
import com.lisadevelopment.lisa.commands.Flag;

public class GuildInfo extends Command {

    public GuildInfo(ChatListener listener) {
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

        private MessageEmbed getMemberInfo(Member member) {
        User user = member.getUser();
        String roleString = member.getRoles().stream().map(IMentionable::getAsMention)
                .collect(Collectors.joining(", "));
        String permissionString = member.getPermissions().stream().map(Permission::getName)
                .collect(Collectors.joining(", "));
        return new EmbedBuilder()
                .setAuthor("Guild Info", null, guild.getIconUrl())
                .setColor('random')
                .setTimestamp(Instant.now())
                .setThumbnail(guild.getIconUrl())
                .setDescription(user.getAsMention() + " **(" + guild.getName() + "#" + user.getDiscriminator() + ")**")
                .addField("Name", guild.getName(), true)
                .addField("Server Region", guild.getRegion(), true)
                .addField("Total members", guild.getMembers().stream().filter(member -> member.getUser()).count(), true)
                .addField("Creation time",
                        guild.getTimeCreated().format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss")), true)
                .addField("Join time",
                        member.getTimeJoined().format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss")), true)
                .addField("Bot accounts", guild.getMembers().stream().filter(member -> member.getUser().isBot()).count(), true)
                .addField("Roles", getRolesByName, false)
                .addField("Permissions", permissionString, false)
                .build();
    }
    
    @Override
    public void treat(ExecutionInstance instance) { //TODO to implement
        
    }
}
