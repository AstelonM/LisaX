package com.lisadevelopment.lisa;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class Lisa {

    public static void main(String[] args) throws LoginException, InterruptedException {
        JDA jda = new JDABuilder(Config.TOKEN).build().awaitReady();
        ChatListener listener = new ChatListener(jda);

        jda.addEventListener(listener);
    }
}
