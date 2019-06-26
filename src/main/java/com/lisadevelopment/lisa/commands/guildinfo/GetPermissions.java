package com.lisadevelopment.lisa.commands.guildinfo;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.Config;
import com.lisadevelopment.lisa.ExecutionInstance;
import com.lisadevelopment.lisa.commands.Command;
import com.lisadevelopment.lisa.commands.Flag;
import com.lisadevelopment.lisa.utils.Format;
import com.lisadevelopment.lisa.utils.GetFromString;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;

import java.time.Instant;
import java.util.stream.Collectors;

public class GetPermissions extends Command {

    public GetPermissions(ChatListener listener) {
        super(listener);
        name = "getPermissions";
        description = "Get the permissions of a member or role.";
        usage = getPrefix() + "getPermissions <role name/mention or member id, name or mention>";
        aliases = new String[] { "getPerms", "permissions", "perms", "getPerm", "permission", "getPermission" };
        examples = getPrefix() + "getPerms Astelon\n" + getPrefix() + "getPerms Member";
        flags = new Flag[] {
                listener.getDeleteFlag(),
                listener.getChainingFlag(),
                listener.getIgnoreFlag()
        };
    }

    private MessageEmbed getPerms(Member target) {
        return new EmbedBuilder()
                .setAuthor(target.getEffectiveName(), null, null)
                .setColor(target.getColor() == null ? Config.BOT_COLOR : target.getColor())
                .setTitle("Permissions")
                .setTimestamp(Instant.now())
                .setFooter(target.getUser().getId(), null)
                .setDescription(target.getPermissions().stream().map(Permission::getName).collect(Collectors.joining(", ")))
                .build();
    }

    private MessageEmbed getPerms(Role target) {
        return new EmbedBuilder()
                .setAuthor(target.getName(), null, null)
                .setColor(target.getColor() == null ? Config.BOT_COLOR : target.getColor())
                .setTitle("Permissions")
                .setTimestamp(Instant.now())
                .setFooter(target.getId(), null)
                .setDescription(target.getPermissions().stream().map(Permission::getName).collect(Collectors.joining(", ")))
                .build();
    }

    @Override
    public void treat(ExecutionInstance instance) {
        Guild guild = instance.getGuild();
        String text = instance.getText();
        if (!text.contains(" ")) {
            MessageEmbed result = getPerms(instance.getMember());
            sendMessage(instance, result);
            instance.setResult(result.getDescription());
        } else {
            text = text.substring(text.indexOf(" ")).trim();
            Member targetMember = GetFromString.getMember(guild, text, Format.MENTION, Format.NAME, Format.ID);
            if (targetMember != null) {
                MessageEmbed result = getPerms(targetMember);
                sendMessage(instance, result);
                instance.setResult(result.getDescription());
            } else {
                Role targetRole = GetFromString.getRole(guild, text, Format.MENTION, Format.NAME, Format.ID);
                if (targetRole != null) {
                    MessageEmbed result = getPerms(targetRole);
                    sendMessage(instance, result);
                    instance.setResult(result.getDescription());
                } else
                    sendMessage(instance, instance.getAuthor().getAsMention() + ", I couldn't find any role or member.");
            }
        }
    }
}
