package com.lisadevelopment.lisa.commands.misc;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.Config;
import com.lisadevelopment.lisa.ExecutionInstance;
import com.lisadevelopment.lisa.Lisa;
import com.lisadevelopment.lisa.commands.Command;
import com.lisadevelopment.lisa.commands.Flag;
import com.lisadevelopment.lisa.utils.JsonUtils;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import okhttp3.*;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Queue;

public class OCR extends Command {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private String jsonBody = "{ \"requests\": [ { \"image\": { \"content\": \"<image>\" }, \"features\": [ { \"type\": \"TEXT_DETECTION\" } ] } ] }";

    OCR(ChatListener listener) {
        super(listener);
        name = "ocr";
        description = "Finds text in a given image.";
        usage = listener.getPrefix() + "ocr <uploadedImage>";
        aliases = new String[]{"opticalCharacterRecognition"};
        examples = listener.getPrefix() + "ocr <send an image with the message>";
        flags = new Flag[]{
                listener.getIgnoreFlag(),
                listener.getDeleteFlag()
        };
    }

    @Override
    public void treat(ExecutionInstance instance) {
        User author = instance.getAuthor();
        Message message = instance.getMessage();
        List<Message.Attachment> attachments = message.getAttachments();
        if (attachments.isEmpty()) {
            sendMessage(instance, author.getAsMention() + " you need to provide an image.");
            return;
        }
        Message.Attachment attachment = attachments.get(0);
        if (!attachment.isImage()) {
            sendMessage(instance, author.getAsMention() + " you need to provide an image.");
            return;
        }
        attachment.retrieveInputStream().thenAccept(inputStream -> {
            String imageText;
            try {
                imageText = Base64.getEncoder().encodeToString(inputStream.readAllBytes());
            } catch (IOException e) {
                sendMessage(instance, author.getAsMention() + " there was an error regarding the image.");
                return;
            }
            RequestBody formBody = RequestBody.create(JSON, jsonBody.replace("<image>", imageText));
            Request request = new Request.Builder()
                    .url("https://vision.googleapis.com/v1/images:annotate?key=" + Config.CLOUD_VISION_KEY)
                    .post(formBody)
                    .build();
            try (Response response = Lisa.httpClient.newCall(request).execute()) {
                if (response.body() == null) {
                    sendMessage(instance, author.getAsMention() + " there was an error retrieving the text.");
                    return;
                }
                String jsonResponse = response.body().string();
                JsonObject object = JsonUtils.parse(jsonResponse).getAsJsonObject();
                JsonArray responses = object.getAsJsonArray("responses");
                JsonObject firstResponse = responses.get(0).getAsJsonObject();
                JsonObject fullTextAnnotation = firstResponse.getAsJsonObject("fullTextAnnotation");
                if (fullTextAnnotation == null) {
                    sendMessage(instance, author.getAsMention() + " I couldn't read any text");
                    return;
                }
                JsonPrimitive textPrimitive = fullTextAnnotation.getAsJsonPrimitive("text");
                String text = textPrimitive.getAsString();
                if (text.length() > Message.MAX_CONTENT_LENGTH) {
                    MessageBuilder messageBuilder = new MessageBuilder(text);
                    Queue<Message> messages = messageBuilder.buildAll(MessageBuilder.SplitPolicy.NEWLINE);
                    author.openPrivateChannel().queue(pm -> {
                        for (Message m: messages)
                            sendMessage(instance, pm, m);
                        sendMessage(instance, author.getAsMention() + " the result was too long, so it was sent " +
                                "in pm. If it's not there, make sure you enabled pms from people who are not your friends.");
                    });
                } else
                    sendMessage(instance, text);
            } catch (Exception e) {
                sendMessage(instance, author.getAsMention() + " there was an error retrieving the text.");
                e.printStackTrace();
            }
        });
    }
}
