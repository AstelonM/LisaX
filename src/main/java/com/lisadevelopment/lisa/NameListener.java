package com.lisadevelopment.lisa;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.api.hooks.EventListener;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NameListener implements EventListener {

    private JDA jda;
    private ArrayList<Long> guildIds;

    public NameListener(JDA jda) {
        this.jda = jda;
        guildIds = new ArrayList<>();
    }

    @Override
    public void onEvent(@Nonnull GenericEvent event) {
        if (event instanceof UserUpdateNameEvent)
            onUserUpdateName((UserUpdateNameEvent) event);
    }

    public void onUserUpdateName(UserUpdateNameEvent event) {
        List<Guild> guilds = guildIds.stream().map(jda::getGuildById).collect(Collectors.toList());
        User user = event.getUser();
        Member member;
        for (Guild guild: guilds) {
            member = guild.getMember(user);
            if (member != null && guild.getSelfMember().canInteract(member) && member.getNickname() == null) {
                try {
                    guild.modifyNickname(member, event.getOldName()).queue();
                } catch (Exception ignore) {}
            }
        }
    }
}
