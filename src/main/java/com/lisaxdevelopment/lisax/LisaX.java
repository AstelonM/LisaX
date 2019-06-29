package com.lisaxdevelopment.lisax;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lisaxdevelopment.lisax.commands.botinfo.BotInfoCommands;
import com.lisaxdevelopment.lisax.commands.guildinfo.GuildInfoCommands;
import com.lisaxdevelopment.lisax.commands.management.ManagementCommands;
import com.lisaxdevelopment.lisax.commands.misc.Currency;
import com.lisaxdevelopment.lisax.commands.misc.MiscCommands;
import com.lisaxdevelopment.lisax.commands.user.UserCommands;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import okhttp3.OkHttpClient;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Date;
import java.util.Properties;

public class LisaX {

    public static final OkHttpClient HTTP_CLIENT =  new OkHttpClient.Builder().build();
    public static Config config;

    public static void main(String[] args) throws LoginException, InterruptedException, IOException {
        Properties properties = new Properties();
        InputStream configFile = LisaX.class.getClassLoader().getResourceAsStream("config.properties");
        if (configFile == null)
            throw new LoginException("config.properties does not exist! Unable to get credentials.");
        properties.load(configFile);
        config = new Config(properties);
        JDA jda = new JDABuilder(config.getToken()).build().awaitReady();
        MongoClient mongoClient = MongoClients.create(config.getMongoUrl());
        ChatListener listener = new ChatListener(jda, mongoClient);
        listener.addCommand(new BotInfoCommands(listener));
        listener.addCommand(new GuildInfoCommands(listener));
        listener.addCommand(new UserCommands(listener));
        listener.addCommand(new MiscCommands(listener));
        listener.addCommand(new ManagementCommands(listener));
        jda.getPresence().setPresence(Activity.watching("for commands, run " + listener.getPrefix() + "help"), false);
        jda.addEventListener(listener);
        // jda.addEventListener(new EmoteListener(mongoClient.getDatabase("lisax")));
        jda.addEventListener(new NameListener(jda, mongoClient.getDatabase("lisax")));
        //Initial currency JSON
        Currency.currencyJSON = (JsonObject) new JsonParser().parse(Currency.getCurrencyJSON(""));
        Currency.lastUpdatedCurrency = Date.from(Instant.now());
    }
}
