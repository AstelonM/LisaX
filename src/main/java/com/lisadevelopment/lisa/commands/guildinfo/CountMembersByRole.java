package com.lisadevelopment.lisa.commands.guildinfo;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.ExecutionInstance;
import com.lisadevelopment.lisa.commands.Command;
import com.lisadevelopment.lisa.commands.Flag;
import com.lisadevelopment.lisa.utils.Format;
import com.lisadevelopment.lisa.utils.GetFromString;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

public class CountMembersByRole extends Command {

    public CountMembersByRole(ChatListener listener) {
        super(listener);
        name = "countMembersByRole";
        description = "Count the members that have a certain role.";
        usage = getPrefix() + "countMembersByRole <role name/mention>";
        examples = getPrefix() + "countMembersByRole Member";
        flags = new Flag[] {
                listener.getDeleteFlag(),
                listener.getChainingFlag(),
                listener.getIgnoreFlag()
        };
    }

    @Override
    public void treat(ExecutionInstance instance) {
        Guild guild = instance.getGuild();
        User author = instance.getAuthor();
        String text = instance.getText();
        if (!text.contains(" ")) {
            sendMessage(instance, author.getAsMention() + " you need to tell me what role you want to count the " +
                    "members of.");
        } else {
            text = text.substring(text.indexOf(" ")).trim();
            Role role = GetFromString.getRole(guild, text, Format.MENTION, Format.NAME, Format.ID);
            if (role == null)
                sendMessage(instance, author.getAsMention() + " I couldn't find any role with the name or id " +
                        text + ".");
            else {
                String result = String.valueOf(guild.getMembersWithRoles(role).size());
                sendMessage(instance, result);
                instance.setResult(result);
            }
        }
    }
}
