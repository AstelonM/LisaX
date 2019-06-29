package com.lisaxdevelopment.lisax.commands.management;

import com.lisaxdevelopment.lisax.ChatListener;
import com.lisaxdevelopment.lisax.ExecutionInstance;
import com.lisaxdevelopment.lisax.commands.Command;
import com.lisaxdevelopment.lisax.commands.Flag;
import com.lisaxdevelopment.lisax.utils.Format;
import com.lisaxdevelopment.lisax.utils.GetFromString;
import com.mongodb.client.model.Filters;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class SetPublicRole extends Command {
    SetPublicRole(ChatListener listener) {
        super(listener);
        name = "setPublicRole";
        description = "Toggles whether a role is public or not.";
        usage = getPrefix() + "setPublicRole <role by name or ID>";
        examples = getPrefix() + "setPublicRole PUBG";
        aliases = new String[] {"spr"};
        flags = new Flag[] {
                listener.getDeleteFlag(),
                listener.getIgnoreFlag(),
                listener.getChainingFlag(),
                listener.getSilentFlag()
        };
    }

    @Override
    public void treat(ExecutionInstance instance) {
        Guild guild = instance.getGuild();
        User author = instance.getAuthor();
        String text = instance.getText();
        if (!text.contains(" "))
            sendMessage(instance, author.getAsMention() + " invalid usage! " + getPrefix() + "setPublicRole <role>");
        else {
            // Get the public role.
            text = text.substring(text.indexOf(" ")).trim();
            Role role = GetFromString.getRole(guild, text, Format.MENTION, Format.NAME, Format.ID);
            Member member = guild.getMember(author);
            if (member == null)
                sendMessage(instance, "ok wat how are you not a member of the guild and running this command here");
            else if (role == null)
                sendMessage(instance, author.getAsMention() + " this role does not exist!");
            else if (!member.hasPermission(Permission.MANAGE_SERVER))
                sendMessage(instance, author.getAsMention() + " you do not have the permission required! (Manage Server)");
            else {
                // Get public roles from database.
                Document server = getListener().getDb()
                        .getCollection("servers")
                        .find(Filters.eq("id", guild.getId()))
                        .first();
                // If server doesn't exist in database, insert a document for it.
                if (server == null)
                    getListener().getDb().getCollection("servers").insertOne(
                            new Document("id", guild.getId())
                                    .append("enforceNicks", false)
                                    .append("publicRoles", new ArrayList<String>())
                    );
                // If no public roles, use blank list.
                List<String> publicRoles = server == null ? new ArrayList<>() : server.getList("publicRoles", String.class);
                // Modify the public role list.
                boolean removed = false;
                if (publicRoles.contains(role.getId())) { publicRoles.remove(role.getId()); removed = true; }
                else publicRoles.add(role.getId());
                // Update on the database.
                boolean acknowledged = getListener().getDb().getCollection("servers").updateOne(
                        Filters.eq("id", guild.getId()),
                        new Document("$set", new Document("publicRoles", publicRoles))
                ).wasAcknowledged();
                if (!acknowledged) sendMessage(instance, author.getAsMention() + " an internal dysfunction occurred.");
                else if (removed)
                    sendMessage(instance, author.getAsMention() + " **" + role.getName() + "** is no longer a public role.");
                else sendMessage(instance, author.getAsMention() + " **" + role.getName() + "** is now a public role.");
            }
        }
    }
}
