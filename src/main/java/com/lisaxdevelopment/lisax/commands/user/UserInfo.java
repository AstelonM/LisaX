package com.lisaxdevelopment.lisax.commands.user;

import com.lisaxdevelopment.lisax.ChatListener;
import com.lisaxdevelopment.lisax.ExecutionInstance;
import com.lisaxdevelopment.lisax.commands.Command;
import com.lisaxdevelopment.lisax.commands.Flag;
import com.lisaxdevelopment.lisax.utils.Format;
import com.lisaxdevelopment.lisax.utils.GetFromString;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class UserInfo extends Command {

    public UserInfo(ChatListener listener) {
        super(listener);
        name = "userInfo";
        description = "Get information about a user.";
        usage = getPrefix() + "userInfo (member mention/name/id)";
        aliases = new String[] { "getUserInfo", "userInformation", "getUserInformation", "memberInfo", "getMemberInfo",
                "memberInformation", "getMemberInformation", "whoIs" };
        examples = getPrefix() + "userInfo\n" + getPrefix() + "userInfo Astelon";
        flags = new Flag[] {
                listener.getIgnoreFlag(),
                listener.getDeleteFlag(),
                listener.getChainingFlag()
        };
    }

    private MessageEmbed getMemberInfo(Member member) {
        User user = member.getUser();
        String roleString = member.getRoles().stream().map(IMentionable::getAsMention)
                .collect(Collectors.joining(", "));
        String permissionString = member.getPermissions().stream().map(Permission::getName)
                .collect(Collectors.joining(", "));
        return new EmbedBuilder()
                .setAuthor("User Info", null, user.getEffectiveAvatarUrl())
                .setColor(member.getColor())
                .setTimestamp(Instant.now())
                .setThumbnail(user.getEffectiveAvatarUrl())
                .setDescription(user.getAsMention() + " **(" + user.getName() + "#" + user.getDiscriminator() + ")**")
                .addField("Name", user.getName(), true)
                .addField("Discriminator", user.getDiscriminator(), true)
                .addField("Id", user.getId(), true)
                .addField("Registration time",
                        member.getTimeCreated().format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss")), true)
                .addField("Join time",
                        member.getTimeJoined().format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss")), true)
                .addField("Bot account", user.isBot() ? "Yes" : "No", true)
                .addField("Roles", roleString, false)
                .addField("Permissions", permissionString, false)
                .build();
    }

    @Override
    public void treat(ExecutionInstance instance) {
        Guild guild = instance.getGuild();
        String text = instance.getText();
        User author = instance.getAuthor();
        if (!text.contains(" ")) {
            sendMessage(instance, getMemberInfo(instance.getMember()));
        } else {
            text = text.substring(text.indexOf(" ")).trim();
            Member target = GetFromString.getMember(guild, text, Format.MENTION, Format.NAME, Format.ID);
            if (target == null) {
                sendMessage(instance, author.getAsMention() + " I found no one to get his info.");
            } else {
                sendMessage(instance, getMemberInfo(target));
            }
        }
    }
}
