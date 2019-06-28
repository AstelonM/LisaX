package com.lisadevelopment.lisa.commands.misc;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.Config;
import com.lisadevelopment.lisa.ExecutionInstance;
import com.lisadevelopment.lisa.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import org.jsoup.Jsoup;

import java.time.Instant;

public class Namemc extends Command {
    Namemc(ChatListener listener) {
        super(listener);
        name = "namemc";
        description = "Get details about a Minecraft account.";
        usage = listener.getPrefix() + "namemc <username>";
        aliases = new String[] { "nmc", "minecraft" };
        examples = listener.getPrefix() + "namemc Astelon";
    }
    @Override
    public void treat(ExecutionInstance instance) {
        if (instance.getText().split(" ").length > 2) {
            sendMessage(instance, "Enter a valid Minecraft username.");
            return;
        }
        try {
            // Request user's UUID (id, name)
            String url = "https://api.mojang.com/users/profiles/minecraft/" + instance.getText().split(" ")[1];
            JsonObject info = new JsonParser().parse(Jsoup.connect(url).ignoreContentType(true).execute().body()).getAsJsonObject();

            // Request user's name history (name, changedToAt)
            url = "https://api.mojang.com/user/profiles/" + info.get("id").getAsString() + "/names";
            JsonArray names = new JsonParser().parse(Jsoup.connect(url).ignoreContentType(true).execute().body()).getAsJsonArray();

            // Create an embed with the UUID, name history and skin of the user.
            EmbedBuilder messageEmbed = new EmbedBuilder()
                    .setTitle("Name Changes")
                    .setColor(Config.BOT_COLOR)
                    .setFooter("Uses the Minecraft API and mc-heads.net")
                    .setImage("https://mc-heads.net/body/" + info.get("id").getAsString());
            for (JsonElement jsonElement : names) {
                String name = jsonElement.getAsJsonObject().get("name").getAsString();
                JsonElement changedToAt = jsonElement.getAsJsonObject().get("changedToAt");
                if (changedToAt == null)
                    messageEmbed = messageEmbed.addField(name, EmbedBuilder.ZERO_WIDTH_SPACE, false);
                else messageEmbed = messageEmbed.addField(
                        name,
                        "Changed on " + Instant.ofEpochMilli(changedToAt.getAsLong()).toString(),
                        false
                );
            }
            messageEmbed = messageEmbed.addField("Skin", EmbedBuilder.ZERO_WIDTH_SPACE, false);
            // Build the embed, message and send.
            MessageBuilder messageBuilder = new MessageBuilder()
                    .setContent("**Skin and name changes for " + info.get("name").getAsString() + ":**")
                    .setEmbed(messageEmbed.build());
            sendMessage(instance, messageBuilder.build());
        } catch (Exception e) {
            sendMessage(instance, "Invalid Minecraft account username! User must be a premium account.");
        }
    }
}
