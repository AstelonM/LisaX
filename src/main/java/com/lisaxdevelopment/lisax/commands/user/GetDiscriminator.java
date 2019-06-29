package com.lisaxdevelopment.lisax.commands.user;

import com.lisaxdevelopment.lisax.ChatListener;
import com.lisaxdevelopment.lisax.ExecutionInstance;
import com.lisaxdevelopment.lisax.commands.Command;
import com.lisaxdevelopment.lisax.commands.Flag;
import com.lisaxdevelopment.lisax.utils.Format;
import com.lisaxdevelopment.lisax.utils.GetFromString;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

public class GetDiscriminator extends Command {

    public GetDiscriminator(ChatListener listener) {
        super(listener);
        name = "getDiscriminator";
        description = "Gets the discriminator (#xxxx) of a user (or your own).";
        usage = getPrefix() + "getDiscriminator (user mention/name/id)";
        aliases = new String[] { "discriminator" };
        examples = getPrefix() + "discriminator\n" + getPrefix() + "discriminator Astelon";
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
        User author = instance.getAuthor();
        User target;
        if (!text.contains(" "))
            target = author;
        else {
            text = text.substring(text.indexOf(" ")).trim();
            Member member = GetFromString.getMember(guild, text, Format.MENTION, Format.NAME, Format.ID);
            if (member == null) {
                sendMessage(instance, author.getAsMention() + " I couldn't find any user to get his discriminator.");
                return;
            }
            target = member.getUser();
        }
        sendMessage(instance, target.getDiscriminator());
        instance.setResult(target.getDiscriminator());
    }
}
