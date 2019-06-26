package com.lisadevelopment.lisa.commands.user;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.ExecutionInstance;
import com.lisadevelopment.lisa.commands.Command;
import com.lisadevelopment.lisa.commands.Flag;
import com.lisadevelopment.lisa.utils.Format;
import com.lisadevelopment.lisa.utils.GetFromString;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public class GetUserId extends Command {

    public GetUserId(ChatListener listener) {
        super(listener);
        name = "id";
        description = "Get the id of a user.";
        usage = getPrefix() + "id (user mention/name)";
        aliases = new String[] { "getId", "userId", "getUserId" };
        examples = getPrefix() + "id\n" + getPrefix() + "id Astelon";
        flags = new Flag[] {
                listener.getIgnoreFlag(),
                listener.getDeleteFlag(),
                listener.getChainingFlag()
        };
    }

    @Override
    public void treat(ExecutionInstance instance) {
        String text = instance.getText();
        Guild guild = instance.getGuild();
        if (!text.contains(" ")) {
            instance.setResult(instance.getAuthor().getId());
            sendMessage(instance, instance.getAuthor().getId());
        } else {
            Member member = GetFromString.getMember(guild, text.substring(text.indexOf(" ")).trim(),
                    Format.MENTION, Format.NAME);
            if (member == null)
                sendMessage(instance, instance.getAuthor().getAsMention() + " I couldn't find any user with that " +
                        "name in this guild.");
            else {
                instance.setResult(member.getUser().getId());
                sendMessage(instance, member.getUser().getId());
            }
        }
    }
}
