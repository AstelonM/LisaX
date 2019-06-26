package com.lisadevelopment.lisa.commands.user;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.ExecutionInstance;
import com.lisadevelopment.lisa.commands.Command;
import com.lisadevelopment.lisa.commands.Flag;
import com.lisadevelopment.lisa.utils.Format;
import com.lisadevelopment.lisa.utils.GetFromString;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public class GetNickname extends Command {

    public GetNickname(ChatListener listener) {
        super(listener);
        name = "getNickname";
        description = "Gets the nickname of a user (or your own).";
        usage = getPrefix() + "getNickname (user mention/name/id)";
        aliases = new String[]{ "getNick" };
        examples = getPrefix() + "getNick\n" + getPrefix() + "getNick Astelon";
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
            result = instance.getMember().getNickname();
        else {
            Member target = GetFromString.getMember(guild, text.substring(text.indexOf(" ")).trim(), Format.MENTION,
                    Format.NAME, Format.ID);
            if (target != null)
                result = target.getNickname();
            else {
                sendMessage(instance, instance.getAuthor().getAsMention() + " I couldn't find anyone.");
                return;
            }
        }
        if (result == null || result.isEmpty()) {
            sendMessage(instance, "No nickname set.");
            instance.setResult("");
        } else {
            sendMessage(instance, result);
            instance.setResult(result);
        }
    }
}
