package com.lisadevelopment.lisa;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lisadevelopment.lisa.commands.botinfo.BotInfoCommands;
import com.lisadevelopment.lisa.commands.guildinfo.GuildInfoCommands;
import com.lisadevelopment.lisa.commands.management.ManagementCommands;
import com.lisadevelopment.lisa.commands.misc.Currency;
import com.lisadevelopment.lisa.commands.misc.MiscCommands;
import com.lisadevelopment.lisa.commands.user.UserCommands;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.Properties;

public class Lisa {

    public static void main(String[] args) throws LoginException, InterruptedException, IOException {
        Properties properties = new Properties();
        properties.load(Lisa.class.getClassLoader().getResourceAsStream("config.properties"));
        String token = properties.getProperty("token");
        Config.fixerKey = properties.getProperty("fixerKey");
        Config.MONGO_URL = properties.getProperty("mongoURL");
        Config.CLOUD_VISION_KEY = properties.getProperty("cloudVisionKey");
        properties.clear();
        JDA jda = new JDABuilder(token).build().awaitReady();
        MongoClient mongoClient = MongoClients.create(Config.MONGO_URL);
        ChatListener listener = new ChatListener(jda, mongoClient);
        listener.addCommand(new BotInfoCommands(listener));
        listener.addCommand(new GuildInfoCommands(listener));
        listener.addCommand(new UserCommands(listener));
        listener.addCommand(new MiscCommands(listener));
        listener.addCommand(new ManagementCommands(listener));
        jda.getPresence().setPresence(Activity.watching("for commands | " + listener.getPrefix() + "help"), false);
        jda.addEventListener(listener);
        jda.addEventListener(new NameListener(jda));
        //Initial currency JSON
        Currency.currencyJSON = (JsonObject) new JsonParser().parse(Currency.getCurrencyJSON(""));
        Currency.lastUpdatedCurrency = Date.from(Instant.now());
    }
}
