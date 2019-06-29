package com.lisaxdevelopment.lisax.commands.user;

import com.lisaxdevelopment.lisax.ChatListener;
import com.lisaxdevelopment.lisax.ExecutionInstance;
import com.lisaxdevelopment.lisax.commands.Command;
import com.lisaxdevelopment.lisax.commands.Flag;
import com.lisaxdevelopment.lisax.utils.Format;
import com.lisaxdevelopment.lisax.utils.GetFromString;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public class GetUsername extends Command {

    public GetUsername(ChatListener listener) {
        super(listener);
        name = "getUsername";
        description = "Gets the username of a user (or your own).";
        usage = getPrefix() + "getUsername (user mention/name/id)";
        aliases = new String[] { "getName" };
        examples = getPrefix() + "getUsername\n" + getPrefix() + "getUsername Astelon";
        flags = new Flag[] {
                listener.getDeleteFlag(),
                listener.getChainingFlag(),
                listener.getIgnoreFlag()
        };
    }

    @Override
    public void treat(ExecutionInstance instance) {
        Guild guild = instance.getGuild();
        String text = instance.getText();
        String result;
        if (!text.contains(" "))
            result = instance.getMember().getUser().getName();
        else {
            Member target = GetFromString.getMember(guild, text.substring(text.indexOf(" ")).trim(),
                    Format.MENTION, Format.NAME, Format.ID);
            if (target != null)
                result = target.getUser().getName();
            else {
                sendMessage(instance, "I couldn't find any user.");
                return;
            }
        }
        sendMessage(instance, result);
        instance.setResult(result);
    }
}
