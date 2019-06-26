package com.lisadevelopment.lisa.commands.guildinfo;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.ExecutionInstance;
import com.lisadevelopment.lisa.commands.Command;
import com.lisadevelopment.lisa.commands.Flag;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;
import java.util.Queue;

public class EmojiList extends Command { //TODO needs a better way of showing them

    public EmojiList(ChatListener listener) {
        super(listener);
        name = "emojiList";
        description = "Get the emojis of a guild (the current one if none specified).";
        usage = getPrefix() + "emojiList";
        aliases = new String[] { "getGuildEmoji", "getGuildEmojis", "guildEmoji", "guildEmojis",
                "getGuildEmote", "getGuildEmotes", "guildEmote", "guildEmotes", "emoteList"};
        examples = getPrefix() + "emojiList";
        flags = new Flag[] {
                listener.getDeleteFlag(),
                listener.getChainingFlag(),
                listener.getIgnoreFlag()
        };
    }

    @Override
    public void treat(ExecutionInstance instance) {
        List<Emote> emojis = instance.getGuild().getEmotes();
        MessageBuilder result = new MessageBuilder();
        for (Emote emoji: emojis)
            result.append(emoji.getAsMention()).append(" - ").append(emoji.getName()).append("\n");
        Queue<Message> messages = result.buildAll(MessageBuilder.SplitPolicy.NEWLINE);
        instance.getAuthor().openPrivateChannel().queue( privateChannel -> {
            for (Message message: messages)
               sendMessage(instance, privateChannel, message);
        });
        instance.setResult(result.toString());
    }
}
