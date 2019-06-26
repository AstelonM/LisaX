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

public class RoleId extends Command {

    public RoleId(ChatListener listener) {
        super(listener);
        name = "roleId";
        description = "Get the id of a role.";
        usage = getPrefix() + "roleId <role mention/name>";
        aliases = new String[] { "getRoleId" };
        examples = getPrefix() + "roleId Member";
        flags = new Flag[] {
                listener.getChainingFlag(),
                listener.getIgnoreFlag(),
                listener.getDeleteFlag()
        };
    }

    @Override
    public void treat(ExecutionInstance instance) {
        Guild guild = instance.getGuild();
        User author = instance.getAuthor();
        String text = instance.getText();
        if (!text.contains(" ")) {
            sendMessage(instance, "I still need that role, you know, " + author.getAsMention() + ".");
        } else {
            text = text.substring(text.indexOf(" ")).trim();
            Role target = GetFromString.getRole(guild, text, Format.MENTION, Format.NAME);
            if (target == null) {
                sendMessage(instance, "I couldn't find any role, " + author.getAsMention() + ".");
                return;
            }
            sendMessage(instance, target.getId());
            instance.setResult(target.getId());
        }
    }
}
