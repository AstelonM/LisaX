package com.lisaxdevelopment.lisax.commands.user;

import com.lisaxdevelopment.lisax.ChatListener;
import com.lisaxdevelopment.lisax.ExecutionInstance;
import com.lisaxdevelopment.lisax.LisaX;
import com.lisaxdevelopment.lisax.commands.Command;
import com.lisaxdevelopment.lisax.commands.Flag;
import com.lisaxdevelopment.lisax.utils.Format;
import com.lisaxdevelopment.lisax.utils.GetFromString;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;

import java.time.Instant;
import java.util.List;

public class GetRoles extends Command {

    public GetRoles(ChatListener listener) {
        super(listener);
        name = "getRoles";
        description = "Get the roles of a member.";
        usage = getPrefix() + "getRoles (member id/name/mention)";
        aliases = new String[] { "roles" };
        examples = getPrefix() + "getRoles\n" + getPrefix() + "getRoles Astelon";
        flags = new Flag[] {
                listener.getDeleteFlag(),
                listener.getIgnoreFlag(),
                listener.getChainingFlag()
        };
    }

    private MessageEmbed getMemberRoles(Member member) {
        EmbedBuilder result = new EmbedBuilder()
                .setAuthor(member.getEffectiveName(), null, member.getUser().getEffectiveAvatarUrl())
                .setColor(LisaX.config.getBotColor())
                .setFooter(member.getUser().getId(), null)
                .setTimestamp(Instant.now());
        List<Role> roles = member.getRoles();
        if (roles.isEmpty())
            result.addField("Roles:", "@everyone", false);
        else {
            StringBuilder rolesString = new StringBuilder(roles.get(0).getAsMention());
            int i;
            for (i = 1; i < roles.size(); i++)
                rolesString.append(" ").append(roles.get(i).getAsMention());
            result.addField("Roles:", rolesString.toString(), false);
        }
        return result.build();
    }

    @Override
    public void treat(ExecutionInstance instance) {
        Guild guild = instance.getGuild();
        Member author = instance.getMember();
        String text = instance.getText();
        if (!text.contains(" ")) {
            sendMessage(instance, getMemberRoles(author));
        } else {
            text = text.substring(text.indexOf(" ")).trim();
            Member target = GetFromString.getMember(guild, text, Format.MENTION, Format.NAME, Format.ID);
            if (target == null)
                sendMessage(instance, author.getAsMention() + " I couldn't find any member.");
            else
                sendMessage(instance, getMemberRoles(target));
        }
    }
}
