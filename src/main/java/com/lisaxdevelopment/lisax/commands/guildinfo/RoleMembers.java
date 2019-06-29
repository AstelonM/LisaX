package com.lisaxdevelopment.lisax.commands.guildinfo;

import com.lisaxdevelopment.lisax.ChatListener;
import com.lisaxdevelopment.lisax.ExecutionInstance;
import com.lisaxdevelopment.lisax.LisaX;
import com.lisaxdevelopment.lisax.commands.Command;
import com.lisaxdevelopment.lisax.commands.Flag;
import com.lisaxdevelopment.lisax.utils.Format;
import com.lisaxdevelopment.lisax.utils.GetFromString;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;

import java.time.Instant;
import java.util.List;

public class RoleMembers extends Command {

    public RoleMembers(ChatListener listener) {
        super(listener);
        name = "roleMembers";
        description = "Get the members that have a certain role (up to 100).";
        usage = getPrefix() + "roleMembers <role name/mention/id>";
        aliases = new String[] { "getMembersWithRole", "membersWithRole", "getMembersByRole", "membersByRole" };
        examples = getPrefix() + "roleMembers Admin";
        flags = new Flag[] {
                listener.getDeleteFlag(),
                listener.getChainingFlag(),
                listener.getIgnoreFlag()
        };
    }

    @Override
    public void treat(ExecutionInstance instance) {
        User author = instance.getAuthor();
        Guild guild = instance.getGuild();
        String text = instance.getText();
        if (!text.contains(" "))
            sendMessage(instance, author.getAsMention() + ", you need to tell me a role.");
        else {
            text = text.substring(text.indexOf(" ")).trim();
            List<Role> roles = GetFromString.getRoles(guild, text, Format.MENTION, Format.NAME, Format.ID);
            if (roles.isEmpty()) {
                sendMessage(instance, author.getAsMention() + " I couldn't find any role.");
                return;
            }
            Role role = roles.get(0);
            List<Member> members = guild.getMembersWithRoles(role);
            if (members.isEmpty())
                sendMessage(instance, "There are no members having the role " + role.getName() + ".");
            else if (members.size() > 100)
                sendMessage(instance, "There are more than 100 members having the role " + role.getName() + ".");
            else {
                EmbedBuilder result = new EmbedBuilder()
                        .setColor(LisaX.config.getBotColor())
                        .setAuthor(author.getName(), null, author.getEffectiveAvatarUrl())
                        .setTimestamp(Instant.now())
                        .setFooter("Role id: " + role.getId(), null);
                boolean first = true;
                StringBuilder memberResult = new StringBuilder();
                for (Member member: members) {
                    if (memberResult.length() + member.getAsMention().length() < MessageEmbed.VALUE_MAX_LENGTH - 1) {
                        if (memberResult.length() != 0)
                            memberResult.append(", ");
                        memberResult.append(member.getAsMention());
                    } else {
                        if (first) {
                            result.addField("Members with the role " + role.getName() + " (" + members.size() + "):",
                                    memberResult.toString(), false);
                            first = false;
                        } else
                            result.addField("", memberResult.toString(), false);
                        memberResult = new StringBuilder(member.getAsMention());
                    }
                }
                if (first)
                    result.addField("Members with the role " + role.getName() + " (" + members.size() + "):",
                            memberResult.toString(), false);
                else
                    result.addField("", memberResult.toString(), false);
                sendMessage(instance, result.build());
            }
        }
    }
}
