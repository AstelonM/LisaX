package com.lisaxdevelopment.lisax.commands.management;

import com.lisaxdevelopment.lisax.ChatListener;
import com.lisaxdevelopment.lisax.ExecutionInstance;
import com.lisaxdevelopment.lisax.commands.Command;
import com.lisaxdevelopment.lisax.commands.Flag;
import com.lisaxdevelopment.lisax.utils.Format;
import com.lisaxdevelopment.lisax.utils.GetFromString;
import com.lisaxdevelopment.lisax.utils.StringUtils;
import com.mongodb.client.model.Filters;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.bson.Document;

import java.util.List;
import java.util.Objects;

public class GiveRole extends Command {
    GiveRole(ChatListener listener) {
        super(listener);
        name = "giveRole";
        description = "Give a role to someone (or yourself).";
        usage = getPrefix() + "giveRole (username) <role name/mention>";
        examples = getPrefix() + "giveRole ThatXuxe Color";
        aliases = new String[] {"gr", "giver"};
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
            sendMessage(instance, author.getAsMention() + " you need to tell me what role you want to give.");
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
            Role role = GetFromString.getRole(guild, roleText, Format.MENTION, Format.NAME, Format.ID);
            if (role == null)
                sendMessage(instance, author.getAsMention() + " I couldn't find any role with the name or ID.");
            else if (member == null)
                sendMessage(instance, author.getAsMention() + " I was unable to find the member.");
            else if (member.getRoles().contains(role))
                sendMessage(instance, author.getAsMention() + they + " already have the specified role! e_e");
            else if (!instance.getMember().canInteract(role) && (
                publicRoles == null || !publicRoles.contains(role.getId()) || member.getIdLong() != author.getIdLong()
            )) sendMessage(instance, author.getAsMention() + " you don't have enough permission for that.. smh.");
            else if (!Objects.requireNonNull(instance.getGuild().getMember(listener.getJda().getSelfUser())).canInteract(role))
                sendMessage(instance, author.getAsMention() + " I can't interact with that role :(");
            else {
                try {
                    instance.getGuild().addRoleToMember(member, role).queue(
                            s -> sendMessage(instance, author.getAsMention() + they + " have been given the role **" + role.getName() + "**."),
                            failure -> sendMessage(
                                    instance, author.getAsMention() + "I was unable to give the role " +
                                    role.getName() + " to" + them + ". Do I have the required permission?"
                            )
                    );
                } catch (Exception e) {
                    sendMessage(
                            instance,
                            author.getAsMention() + " ensure I have enough permissions to give the role to " +
                            them + ". Managed roles cannot be given or taken from users."
                    );
                }
            }
        }
    }
}
