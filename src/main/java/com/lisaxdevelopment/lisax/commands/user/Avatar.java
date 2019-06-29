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
import net.dv8tion.jda.api.entities.User;

public class Avatar extends Command {

    public Avatar(ChatListener listener) {
        super(listener);
        name = "avatar";
        description = "Get the avatar of a user.";
        usage = getPrefix() + "avatar (user mention/name/id)";
        aliases = new String[] { "av", "getAvatar" };
        examples = getPrefix() + "av\n" + getPrefix() + "av Astelon";
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
                sendMessage(instance, author.getAsMention() + " I couldn't find any user in here.");
                return;
            }
            target = member.getUser();
        }
        sendMessage(instance, new EmbedBuilder()
                .setColor(LisaX.config.getBotColor())
                .setAuthor(target.getName(), null, target.getEffectiveAvatarUrl())
                .setImage(target.getEffectiveAvatarUrl() + "?size=2048")
                .build());
    }
}
