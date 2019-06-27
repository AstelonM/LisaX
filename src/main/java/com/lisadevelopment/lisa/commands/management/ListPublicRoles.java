package com.lisadevelopment.lisa.commands.management;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.Config;
import com.lisadevelopment.lisa.ExecutionInstance;
import com.lisadevelopment.lisa.commands.Command;
import com.lisadevelopment.lisa.commands.Flag;
import com.mongodb.client.model.Filters;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import org.bson.Document;

import java.util.List;

public class ListPublicRoles extends Command {
    ListPublicRoles(ChatListener listener) {
        super(listener);
        name = "listPublicRoles";
        description = "List the public roles of the guild.";
        usage = getPrefix() + "listPublicRoles";
        examples = getPrefix() + "listPublicRoles";
        aliases = new String[] {"lpr"};
        flags = new Flag[] {
                listener.getDeleteFlag(),
                listener.getIgnoreFlag()
        };
    }

    @Override
    public void treat(ExecutionInstance instance) {
        Guild guild = instance.getGuild();
        User author = instance.getAuthor();
        String text = instance.getText();
        if (text.contains(" "))
            sendMessage(instance, author.getAsMention() + " invalid usage! Just " + getPrefix() + "listPublicRoles");
        else {
            // Get public roles from database.
            Document server = getListener().getDb()
                    .getCollection("servers")
                    .find(Filters.eq("id", guild.getId()))
                    .first();
            List<String> publicRoles = server == null ? null : server.getList("publicRoles", String.class);
            if (publicRoles == null || publicRoles.size() == 0)
                sendMessage(instance, author.getAsMention() + " this server has no public roles enabled.");
            else {
                MessageEmbed messageEmbed = new EmbedBuilder()
                        .setColor(Config.BOT_COLOR)
                        .setTitle("Public roles for " + guild.getName())
                        .setDescription(publicRoles.stream().reduce("", (x, y) -> {
                            Role role = guild.getRoleById(y);
                            if (role == null) return "\nUnidentified role: **" + y + "**";
                            else return x + "\n**" + role.getAsMention() + "**";
                        }))
                        .build();
                Message message = new MessageBuilder()
                        .setEmbed(messageEmbed)
                        .setContent(author.getAsMention() + " list of public roles:")
                        .build();
                sendMessage(instance, message);
            }
        }
    }
}
