package com.lisaxdevelopment.lisax;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.bson.Document;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class NameListener implements EventListener {

    private JDA jda;
    private MongoCollection<Document> servers;

    NameListener(JDA jda, MongoDatabase mongoDatabase) {
        this.jda = jda;
        this.servers = mongoDatabase.getCollection("servers");
    }

    @Override
    public void onEvent(@Nonnull GenericEvent event) {
        if (event instanceof UserUpdateNameEvent)
            onUserUpdateName((UserUpdateNameEvent) event);
    }

    private void onUserUpdateName(UserUpdateNameEvent event) {
        FindIterable<Document> servers = this.servers.find(Filters.eq("enforceNicks", true));
        if (servers.first() == null) return;
        User user = event.getUser();
        servers.forEach((Consumer<Document>) server -> {
            Guild guild = jda.getGuildById(server.getString("id"));
            if (guild == null) {
                this.servers.deleteOne(Filters.eq("id", server.getString("id")));
                return;
            }
            Member member = guild.getMember(user);
            if (member != null && guild.getSelfMember().canInteract(member) && member.getNickname() == null) {
                try {
                    guild.modifyNickname(member, event.getOldName()).queue();
                } catch (Exception ignore) {}
            }
        });
    }
}
