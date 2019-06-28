package com.lisadevelopment.lisa.commands.management;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.ExecutionInstance;
import com.lisadevelopment.lisa.commands.Command;
import com.lisadevelopment.lisa.commands.Flag;
import com.lisadevelopment.lisa.utils.Format;
import com.lisadevelopment.lisa.utils.GetFromString;
import com.lisadevelopment.lisa.utils.StringUtils;
import com.mongodb.client.model.Filters;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import org.bson.Document;
import java.util.List;
import java.util.Objects;

public class TakeRole extends Command {
    TakeRole(ChatListener listener) {
        super(listener);
        name = "takeRole";
        description = "Take a role from someone (or yourself).";
        usage = getPrefix() + "takeRole (username) <role name/mention>";
        examples = getPrefix() + "takeRole ThatXuxe Color";
        aliases = new String[] {"tr", "taker"};
        flags = new Flag[] {
                listener.getDeleteFlag(),
                listener.getChainingFlag(),
                listener.getIgnoreFlag(),
                listener.getSilentFlag()
        };
    }

    @Override
    public void treat(ExecutionInstance instance) {
        Guild guild = instance.getGuild();
        User author = instance.getAuthor();
        String text = instance.getText();
        if (!text.contains(" "))
            sendMessage(instance, author.getAsMention() + " you need to tell me what role you want to take.");
        else if (!instance.getMember().hasPermission(Permission.MANAGE_ROLES))
            sendMessage(instance, author.getAsMention() + " you don't have enough permission for that..");
        else {
            // Attempt to find a user.
            text = text.substring(text.indexOf(" ")).trim();
            String memberText = StringUtils.firstWord(text);
            Member member = GetFromString.getMember(guild, memberText, Format.MENTION, Format.NAME, Format.ID);
            String roleText = member != null ? text.substring(memberText.length()).trim() : text;
            if (member == null) member = guild.getMember(author);
            String they = member != null && member.getIdLong() == author.getIdLong() ? " you" : " they";
            String them = member != null && member.getIdLong() == author.getIdLong() ? " you" : " them";
            // Get public roles from database.
            Document server = getListener().getDb()
                    .getCollection("servers")
                    .find(Filters.eq("id", guild.getId()))
                    .first();
            List<String> publicRoles = server == null ? null : server.getList("publicRoles", String.class);
            text = text.substring(text.indexOf(" ")).trim();
            Role role = GetFromString.getRole(guild, text, Format.MENTION, Format.NAME, Format.ID);
            if (role == null)
                sendMessage(instance, author.getAsMention() + " I couldn't find any role with the name or ID.");
            else if (member == null)
                sendMessage(instance, author.getAsMention() + " I was unable to find the member.");
            else if (!member.getRoles().contains(role))
                sendMessage(instance, author.getAsMention() + they + " don't have the specified role! e_e");
            else if (!instance.getMember().canInteract(role) && (
                publicRoles == null || !publicRoles.contains(role.getId()) || member.getIdLong() != author.getIdLong()
            )) sendMessage(instance, author.getAsMention() + " you don't have enough permission for that.. smh.");
            else if (!Objects.requireNonNull(instance.getGuild().getMember(listener.getJda().getSelfUser())).canInteract(role))
                sendMessage(instance, author.getAsMention() + " I can't interact with that role :(");
            else {
                try {
                    instance.getGuild().removeRoleFromMember(member, role).queue(
                            s -> sendMessage(instance, author.getAsMention() + they + " no longer have the role **" + role.getName() + "**."),
                            failure -> sendMessage(
                                    instance, author.getAsMention() + "I was unable to take the role " +
                                    role.getName() + " from" + them + ". Do I have the required permission?"
                            )
                    );
                } catch (Exception e) {
                    sendMessage(
                            instance,
                            author.getAsMention() + " ensure I have enough permissions to take the role from" +
                            them + ". Managed roles cannot be given or taken from users."
                    );
                }
            }
        }
    }
}
