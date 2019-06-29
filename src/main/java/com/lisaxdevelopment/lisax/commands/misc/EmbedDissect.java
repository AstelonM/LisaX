package com.lisaxdevelopment.lisax.commands.misc;

import com.lisaxdevelopment.lisax.ChatListener;
import com.lisaxdevelopment.lisax.ExecutionInstance;
import com.lisaxdevelopment.lisax.commands.Command;
import com.lisaxdevelopment.lisax.commands.Flag;
import com.lisaxdevelopment.lisax.utils.ImageUtils;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

import java.time.OffsetDateTime;

public class EmbedDissect extends Command { //TODO needs improving :v

    private ExecutionInstance instance;
    private String[] args;
    private MessageEmbed embed;

    EmbedDissect(ChatListener listener){
        super(listener);
        this.name = "embedDissect";
        this.description = "Get raw text from an embed message for an easy copy-paste. Copies discord formats too.";
        this.usage = listener.getPrefix() + "ed <messageID> <title|description|footer|footerImage|image|timestamp|author|authorImage|color|fieldName|fieldValue> [fieldIndex]";
        this.aliases = new String[] { "ed", "embedD", "eDissect", "embedContent", "getEmbedContent", "ec" };
        this.examples = listener.getPrefix() + "ed 592795299697917962 description";
        flags = new Flag[] {
                listener.getDeleteFlag(),
                listener.getChainingFlag(),
                listener.getIgnoreFlag()
        };
    }

    @Override
    public void treat(ExecutionInstance i){
        instance = i;
        args = instance.getMessage().getContentRaw().split("\\s"); // args contain the command, so to check real args, start at 1 and not 0
        // Verify if args are right, if they're not return
        if(!argsAreRight()){
            sendMessage(instance,"❌ | Invalid usage. `" + getUsage() + "`");
            return;
        }else if(!msgHasEmbed()){
            sendMessage(instance, "❌ | That message does not have an embed.");
            return;
        }
        embed = instance.getChannel().retrieveMessageById(args[1]).complete().getEmbeds().get(0);
        // args[2] is which part of the embed the user wants to fetch.
        switch(args[2]){
            case "title":
                sendTitle();
                break;
            case "description":
            case "desc":
                sendDescription();
                break;
            case "footer":
                sendFooter();
                break;
            case "footerImage":
            case "footerImg":
                sendFooterImg();
                break;
            case "image":
                sendImage();
                break;
            case "timestamp":
                sendTimestamp();
                break;
            case "author":
                sendAuthor();
                break;
            case "authorImage":
            case "authorImg":
                sendAuthorImage();
                break;
            case "color":
                sendColor();
                break;
            case "fieldValue":
            case "fieldValues":
                sendFieldValues();
                break;
            case "fieldName":
            case "fieldNames":
                sendFieldNames();
                break;
            default:
                sendMessage(instance,"❌ | Invalid type. **Usage:** `"+getUsage()+"`");

        }
    }

   private boolean argsAreRight() {
       if(args.length == 1) return false;
       else if(isNumeric(args[1] ) && args.length == 2 && msgHasEmbed()){
           instance.getChannel().sendMessage(instance.getChannel().retrieveMessageById(args[1]).complete().getEmbeds().get(0)).queue();
           return true;
       }
       return isNumeric(args[1]) && args.length >= 3;
   }

   private boolean isNumeric(String possibleNumber) {
       for (char ch : possibleNumber.toCharArray())
           if (Character.isDigit(ch)) return true;
       return false;
   }

   private boolean msgHasEmbed(){
        // If the List is empty, then return false
       try{
           return !instance.getChannel().retrieveMessageById(args[1]).complete().getEmbeds().isEmpty();
       }catch(ErrorResponseException e){
           sendMessage(instance,"❌ | No message found.");
           return false;
       }

   }

   private void sendTitle(){
        if(embed.getTitle() == null){
            sendMessage(instance, "❌ | That embed does not seem to have a title... \uD83E\uDD14");
            return;
        }
        sendMessage(instance, "```"+embed.getTitle()+"```");
   }

   private void sendDescription(){
       if(embed.getDescription() == null){
           sendMessage(instance, "❌ | That embed does not seem to have a description... \uD83E\uDD14");
           return;
       }
       sendMessage(instance,"```"+embed.getDescription()+"```");
   }

   private void sendFooter(){
       if(embed.getFooter() == null){
           sendMessage(instance, "❌ | That embed does not seem to have a footer... \uD83E\uDD14");
           return;
       }else if(embed.getFooter().getText() == null){
           sendMessage(instance, "❌ | I did find a footer in that embed, but apparently it has no text. \uD83E\uDD14");
           return;
       }
       sendMessage(instance, "```"+embed.getFooter().getText()+"```");
   }

   private void sendFooterImg(){
       if(embed.getFooter() == null){
           sendMessage(instance, "❌ | That embed does not seem to have a footer... \uD83E\uDD14");
           return;
       }else if(embed.getFooter().getIconUrl() == null){
           sendMessage(instance, "❌ | No images in that footer... \uD83E\uDD14");
           return;
       }
       sendMessage(instance, embed.getFooter().getIconUrl());
   }

   private void sendImage(){
        if(embed.getImage() == null){
            sendMessage(instance, "❌ | That embed has no image I suppose... \uD83E\uDD14");
            return;
        }
        sendMessage(instance, embed.getImage().getUrl());
   }

   private void sendTimestamp(){
        if(embed.getTimestamp() == null){
            sendMessage(instance, "❌ | No timestamp found in that embed. Did you mean something else?");
            return;
        }
       OffsetDateTime timestamp = embed.getTimestamp();
       // Add a 0 every time the length of the time is 1. For example, 09:11:24 makes more sense than 9:11:24
       String hours = String.valueOf(embed.getTimestamp().getHour()).toCharArray().length == 2 ? String.valueOf(embed.getTimestamp().getHour()) : "0"+embed.getTimestamp().getHour();
       String minutes = String.valueOf(embed.getTimestamp().getMinute()).toCharArray().length == 2 ? String.valueOf(embed.getTimestamp().getMinute()) : "0"+embed.getTimestamp().getMinute();
       String seconds = String.valueOf(embed.getTimestamp().getSecond()).toCharArray().length == 2 ? String.valueOf(embed.getTimestamp().getSecond()) : "0"+embed.getTimestamp().getSecond();
       sendMessage(instance, "```Date: "+timestamp.getDayOfWeek()+" the "+ timestamp.getDayOfMonth() + " of " + timestamp.getMonth() + ", of " + timestamp.getYear() + ".\nTime: " + hours + ":" + minutes + ":" + seconds + " GMT\nRaw: "+embed.getTimestamp()+"```");
   }

   private void sendAuthor(){
        if(embed.getAuthor() == null){
            sendMessage(instance, "❌ | I think that embed has no author part. Are you asking for the right embed?");
            return;
        }else if(embed.getAuthor().getName() == null){
            sendMessage(instance, "❌ | There's an author but no name... Interesting? Perhaps try `authorImage`.");
            return;
        }
        sendMessage(instance, "```"+embed.getAuthor().getName()+"```");
   }

   private void sendAuthorImage(){
       if(embed.getAuthor() == null){
           sendMessage(instance, "❌ | I think that embed has no author part. Are you asking for the right embed?");
           return;
       }else if(embed.getAuthor().getIconUrl() == null){
           sendMessage(instance, "❌ | There's no image in the author part. Hmm \uD83E\uDD14");
           return;
       }
       sendMessage(instance, embed.getAuthor().getIconUrl());
   }

   private void sendColor(){
        if(embed.getColor() == null){
            sendMessage(instance,"❌ | That embed has no color.");
            return;
        }
       byte[] imageBytes = ImageUtils.createColoredRectangle(200,50,embed.getColor());
       String hex = "#" + Integer.toHexString(embed.getColor().getRGB()).substring(2);
       if(imageBytes == null){
           sendMessage(instance, "❌ | Something went wrong while creating the image containing the color. **Here's the color HEX:** "+hex);
           return;
       }
       instance.getChannel().sendMessage("**HEX:** "+hex).addFile(imageBytes,"color.png").queue();
   }

    private void sendFieldNames(){
        if(badVerifyFieldUsage()) return;
        sendMessage(instance,"```"+embed.getFields().get(Integer.parseInt(args[3])-1).getName()+"```");
    }

   private void sendFieldValues(){
        if(badVerifyFieldUsage()) return;
        sendMessage(instance,"```"+embed.getFields().get(Integer.parseInt(args[3])-1).getValue()+"```");
   }

   private boolean badVerifyFieldUsage(){
       if(embed.getFields().isEmpty()){
           sendMessage(instance,"❌ | Apparently that embed has no fields.");
           return true;
       }else if(args.length <= 3){
           sendMessage(instance,"❌ | You must add the number of the field in the embed in order for me to send it to you. `"+getPrefix()+"ed "+args[1]+" field <fieldIndex>`");
           return true;
       }else if(!isNumeric(args[3])){
           sendMessage(instance,"❌ | Would be nice if the field number were an actual **number**.");
           return true;
       }else if(Integer.parseInt(args[3]) < 1){
           sendMessage(instance, "❌ | Well, the field index (number) here starts at 1. Just like in Lua.");
           return true;
       }else if(Integer.parseInt(args[3]) > embed.getFields().size()){
           sendMessage(instance,"❌ | I can't find field #"+args[3]+".");
           return true;
       }
       return false;
   }
}
