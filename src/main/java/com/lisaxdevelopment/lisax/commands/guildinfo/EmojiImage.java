package com.lisaxdevelopment.lisax.commands.guildinfo;

import com.lisaxdevelopment.lisax.ChatListener;
import com.lisaxdevelopment.lisax.ExecutionInstance;
import com.lisaxdevelopment.lisax.LisaX;
import com.lisaxdevelopment.lisax.commands.Command;
import com.lisaxdevelopment.lisax.commands.Flag;
import com.lisaxdevelopment.lisax.utils.Format;
import com.lisaxdevelopment.lisax.utils.GetFromString;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;

import java.util.List;

public class EmojiImage extends Command {

    public EmojiImage(ChatListener listener) {
        super(listener);
        name = "emojiImage";
        description = "Get the image of a custom emote.";
        usage = getPrefix() + "emojiImage <emoji mention/name/id>";
        aliases = new String[] { "getEmojiImage", "ei", "emoteImage", "getEmoteImage" };
        examples = getPrefix() + "emojiImage tom";
        flags = new Flag[] {
                listener.getDeleteFlag(),
                listener.getChainingFlag(),
                listener.getIgnoreFlag()
        };
    }

    @Override
    public void treat(ExecutionInstance instance) {
        List<Emote> emotes = GetFromString.getGuildEmotes(instance.getGuild(), instance.getText(), Format.MENTION,
                Format.NAME, Format.ID);
        if (emotes.isEmpty())
            sendMessage(instance, "You need to provide a custom emoji to view.");
        else {
            Emote target = emotes.get(0);
            EmbedBuilder builder = new EmbedBuilder();
            builder.setColor(LisaX.config.getBotColor()).setAuthor(target.getName(), null, target.getImageUrl()).
                    setImage(target.getImageUrl());
            sendMessage(instance, builder.build());
        }
    }
}
