package com.lisaxdevelopment.lisax.commands.user;

import com.lisaxdevelopment.lisax.ChatListener;
import com.lisaxdevelopment.lisax.ExecutionInstance;
import com.lisaxdevelopment.lisax.commands.Command;
import com.lisaxdevelopment.lisax.commands.Flag;
import com.mongodb.client.model.Filters;
import net.dv8tion.jda.api.entities.User;
import org.bson.Document;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;

public class Note extends Command {
    Note(ChatListener listener) {
        super(listener);
        name = "note";
        description = "Make your own notes about stuff.";
        usage = getPrefix() + "note (clear, else some personal notes)";
        examples = getPrefix() + "note need to go to the shop soon";
        aliases = new String[] {"notes"};
        flags = new Flag[] {
                listener.getDeleteFlag(),
                listener.getChainingFlag(),
                listener.getIgnoreFlag(),
                listener.getSilentFlag()
        };
    }

    @Override
    public void treat(ExecutionInstance instance) {
        User author = instance.getAuthor();
        String text = instance.getText();
        // Get user from database.
        Document user = getListener().getDb()
                .getCollection("users")
                .find(Filters.eq("id", author.getId()))
                .first();
        if (user == null) getListener().getDb().getCollection("users").insertOne(
                new Document("id", author.getId()).append("note", "")
        );
        String note;
        try {
            note = user == null ? "" : decrypt(user.getString("note"), author.getId());
        } catch (Exception e) { sendMessage(instance, "Failed to decrypt your notes.."); e.printStackTrace(); return; }
        if (!text.contains(" ")) {
            if (note.equals("")) sendMessage(instance, "Your notes are empty!");
            else sendMessage(instance, "**Your notes:**\n" + note);
        } else if (text.contains(" clear")) {
            boolean acknowledged = getListener().getDb().getCollection("users")
                    .updateOne(Filters.eq("id", author.getId()), new Document("$set", new Document("note", "")))
                    .wasAcknowledged();
            if (acknowledged) sendMessage(instance, "Your notes have been cleared!");
            else sendMessage(instance, "Looks like something went wrong when clearing your notes.");
        } else {
            try {
                text = text.substring(text.indexOf(" ")).trim();
                boolean acknowledged = getListener().getDb().getCollection("users")
                        .updateOne(Filters.eq("id", author.getId()), new Document("$set", new Document("note", encrypt(text, author.getId()))))
                        .wasAcknowledged();
                if (acknowledged) sendMessage(instance, "Your notes have been updated!");
                else sendMessage(instance, "Looks like something went wrong when updating your notes.");
            } catch (Exception e) {
                sendMessage(instance, "Failed to decrypt your notes..");
            }
        }
    }

    private String secretKey = listener.getJda().getToken();
    private String encrypt(String text, String salt) throws Exception {
        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
        return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes(StandardCharsets.UTF_8)));
    }
    private String decrypt(String text, String salt) throws Exception {
        if (text == null || text.equals("")) return "";
        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
        return new String(cipher.doFinal(Base64.getDecoder().decode(text)));
    }
}
