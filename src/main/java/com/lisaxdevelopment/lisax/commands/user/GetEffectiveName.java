package com.lisaxdevelopment.lisax.commands.user;

import com.lisaxdevelopment.lisax.ChatListener;
import com.lisaxdevelopment.lisax.ExecutionInstance;
import com.lisaxdevelopment.lisax.commands.Command;
import com.lisaxdevelopment.lisax.commands.Flag;
import com.lisaxdevelopment.lisax.utils.Format;
import com.lisaxdevelopment.lisax.utils.GetFromString;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public class GetEffectiveName extends Command {

    public GetEffectiveName(ChatListener listener) {
        super(listener);
        name = "getEffectiveName";
        description = "Get the nickname of someone, or his username if he has no nickname.";
        usage = getPrefix() + "getEffectiveName (user mention/name/id)";
        aliases = new String[] { "effectiveName" };
        examples = getPrefix() + "effectiveName\n" + getPrefix() + "effectiveName Astelon";
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
            result = instance.getMember().getEffectiveName();
        else {
            Member target = GetFromString.getMember(guild, text.substring(text.indexOf(" ")).trim(),
                    Format.MENTION, Format.NAME, Format.ID);
            if (target != null)
                result = target.getEffectiveName();
            else {
                sendMessage(instance, "I couldn't find any member, make sure what you wrote is correct.");
                return;
            }
        }
        sendMessage(instance, result);
        instance.setResult(result);
    }
}
