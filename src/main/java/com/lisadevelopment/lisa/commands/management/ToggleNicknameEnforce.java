package com.lisadevelopment.lisa.commands.management;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.ExecutionInstance;
import com.lisadevelopment.lisa.commands.Command;
import com.lisadevelopment.lisa.commands.Flag;
import com.lisadevelopment.lisa.utils.Format;
import com.lisadevelopment.lisa.utils.GetFromString;
import com.mongodb.client.model.Filters;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import org.bson.Document;

import java.util.ArrayList;

public class ToggleNicknameEnforce extends Command {
    ToggleNicknameEnforce(ChatListener listener) {
        super(listener);
        name = "toggleNicknameEnforce";
        description = "Toggles whether nickname enforcement is enabled.";
        usage = getPrefix() + "toggleNicknameEnforce";
        examples = getPrefix() + "toggleNicknameEnforce";
        aliases = new String[] {"tne", "toggleNickEnforce", "nickEnforce", "nicknameEnforce"};
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
            sendMessage(instance, author.getAsMention() + " invalid usage! Just " + getPrefix() + "toggleNicknameEnforce");
        else {
            Member member = guild.getMember(author);
            if (member == null)
                sendMessage(instance, "ok wat how are you not a member of the guild and running this command here");
            else if (!member.hasPermission(Permission.MANAGE_SERVER))
                sendMessage(instance, author.getAsMention() + " you do not have the permission required! (Manage Server)");
            else {
                // Get nickname enforcement status from database.
                Document server = getListener().getDb().getCollection("servers")
                        .find(Filters.eq("id", guild.getId()))
                        .first();
                // If server doesn't exist in database, insert a document for it.
                if (server == null) {
                    getListener().getDb().getCollection("servers").insertOne(
                            new Document("id", guild.getId()).append("enforceNicks", true)
                                    .append("publicRoles", new ArrayList<String>())
                    );
                    sendMessage(instance, author.getAsMention() + " nickname enforcement is now enabled.");
                } else {
                    boolean enabled = server.getBoolean("enforceNicks");
                    boolean target = !enabled;
                    // Update on the database.
                    boolean acknowledged = getListener().getDb().getCollection("servers").updateOne(
                            Filters.eq("id", guild.getId()),
                            new Document("$set", new Document("enforceNicks", target))
                    ).wasAcknowledged();
                    if (!acknowledged) sendMessage(instance, author.getAsMention() + " an internal dysfunction occurred.");
                    else if (target) sendMessage(instance, author.getAsMention() + " nickname enforcement is now enabled.");
                    else sendMessage(instance, author.getAsMention() + " nickname enforcement is now disabled.");
                }
            }
        }
    }
}
