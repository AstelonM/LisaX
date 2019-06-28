package com.lisadevelopment.lisa.commands.misc;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.Config;
import com.lisadevelopment.lisa.ExecutionInstance;
import com.lisadevelopment.lisa.commands.Command;
import net.dv8tion.jda.api.entities.Message;
import okhttp3.*;

import java.io.IOException;

public class OCR extends Command {
    private OkHttpClient client = new OkHttpClient();
    private String jsonBody = "{ \"requests\": [ { \"features\": [ { \"type\": \"TEXT_DETECTION\" } ], \"image\": { \"source\": { \"imageUri\": <image> } } } ] }";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    OCR(ChatListener listener){
        super(listener);
        this.name = "ocr";
        this.description = "Detects and sends text from a sent image or image URL.";
        this.usage = listener.getPrefix() + "ocr <uploadedImage|imageUrl>";
        this.examples = listener.getPrefix() + "ocr <imageUrl>";
    }

    @Override
    public void treat(ExecutionInstance instance) {
        String[] args = instance.getMessage().getContentRaw().split("\\s");
        if(instance.getMessage().getAttachments().isEmpty() && args.length > 1){
            sendMessage(instance,"This is not available yet. Please use attachments for now.");
        }else if(!(instance.getMessage().getAttachments().size() > 0)){
            sendMessage(instance,"You require an image attached to the command message.");
            return;
        }
        Message.Attachment attachment = instance.getMessage().getAttachments().get(0);
        attachment.retrieveAsIcon().whenCompleteAsync( (icon, exception) -> {
            if(exception != null){
                sendMessage(instance,"Something went wrong.");
                return;
            }
            String response = processRequest(icon.getEncoding());
            if(response == null){
                sendMessage(instance,"Something went wrong.");
                return;
            }
            // TODO send the response (requires parsing the json response)
        });
    }

    private String processRequest(String encodedImage){
        RequestBody formBody = RequestBody.create(JSON, jsonBody.replace("<image>", encodedImage));
        Request request = new Request.Builder()
                .url("https://vision.googleapis.com/v1/images:annotate?key="+Config.CLOUD_VISION_KEY)
                .post(formBody)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if(response.body() == null) return null;
            return response.body().string();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
