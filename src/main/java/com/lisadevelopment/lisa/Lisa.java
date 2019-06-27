package com.lisadevelopment.lisa;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lisadevelopment.lisa.commands.Currency;
import com.lisadevelopment.lisa.commands.botinfo.BotInfoCommands;
import com.lisadevelopment.lisa.commands.guildinfo.GuildInfoCommands;
import com.lisadevelopment.lisa.commands.user.UserCommands;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.Properties;

public class Lisa {

    public static void main(String[] args) throws LoginException, InterruptedException, IOException {
        Properties properties = new Properties();
        FileReader fileReader = new FileReader("config.properties");
        properties.load(fileReader);
        String token = properties.getProperty("token");
        Config.fixerKey = properties.getProperty("fixerKey");
        fileReader.close();
        properties.clear();
        JDA jda = new JDABuilder(token).build().awaitReady();
        ChatListener listener = new ChatListener(jda);
        listener.addCommand(new BotInfoCommands(listener));
        listener.addCommand(new GuildInfoCommands(listener));
        listener.addCommand(new UserCommands(listener));
        listener.addCommand(new Currency(listener));
        jda.addEventListener(listener);
        //Initial currency JSON
        Config.currencyJSON = (JsonObject) new JsonParser().parse(Currency.getCurrencyJSON(""));
        Config.lastUpdatedCurrency = Date.from(Instant.now());
    }
}
