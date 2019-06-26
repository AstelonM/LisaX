package com.lisadevelopment.lisa.commands.guildinfo;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.Config;
import com.lisadevelopment.lisa.ExecutionInstance;
import com.lisadevelopment.lisa.commands.Command;
import com.lisadevelopment.lisa.commands.Flag;
import com.lisadevelopment.lisa.utils.Format;
import com.lisadevelopment.lisa.utils.GetFromString;
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
            builder.setColor(Config.BOT_COLOR).setAuthor(target.getName(), null, target.getImageUrl()).
                    setImage(target.getImageUrl());
            sendMessage(instance, builder.build());
        }
    }
}
