package com.lisaxdevelopment.lisax.commands.guildinfo;

import com.lisaxdevelopment.lisax.ChatListener;
import com.lisaxdevelopment.lisax.ExecutionInstance;
import com.lisaxdevelopment.lisax.commands.Command;
import com.lisaxdevelopment.lisax.commands.Flag;
import com.lisaxdevelopment.lisax.utils.Format;
import com.lisaxdevelopment.lisax.utils.GetFromString;
import com.lisaxdevelopment.lisax.utils.ImageUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;

import java.awt.*;

public class GetColor extends Command {

    private static final int WIDTH = 50;
    private static final int HEIGHT = 20;

    public GetColor(ChatListener listener) {
        super(listener);
        name = "getColourCode";
        description = "Get the HEX string representing the colour of the user/role (or of yourself :P)";
        usage = getPrefix() + "getColourCode (user mention/name/id or role mention/name/id)";
        aliases = new String[] {
                "getColorCode", "getColour", "getColor", "colour", "color", "getRoleColour", "getRoleColor",
                "roleColor", "roleColour"
        };
        flags = new Flag[] {
                listener.getDeleteFlag(),
                listener.getChainingFlag(),
                listener.getIgnoreFlag()
        };
    }

    @Override
    public void treat(ExecutionInstance instance) {
        MessageChannel channel = instance.getChannel();
        Guild guild = instance.getGuild();
        Member author = instance.getMember();
        Color color;
        String text = instance.getText();
        if (!text.contains(" "))
            color = author.getColor();
        else {
            text = text.substring(text.indexOf(" ")).trim();
            Member member = GetFromString.getMember(guild, text, Format.MENTION, Format.NAME, Format.ID);
            if (member != null)
                color = member.getColor();
            else {
                Role role = GetFromString.getRole(guild, text, Format.MENTION, Format.NAME, Format.ID);
                if (role != null)
                    color = role.getColor();
                else {
                    sendMessage(instance, author.getAsMention() + " I couldn't find any member or role.");
                    return;
                }
            }
        }
        if (color == null)
            sendMessage(instance, "There is no colour set.");
        else {
            byte[] imageOutput = null;
            if (!instance.getFlags().contains(listener.getChainedFlag()))
                imageOutput = ImageUtils.createColoredRectangle(WIDTH, HEIGHT, color);
            String result = "#" + Integer.toHexString(color.getRGB()).substring(2);
            if (instance.isShouldReply()) {
                if (imageOutput != null)
                    channel.sendMessage(result).addFile(imageOutput, "colour.png").queue();
                else
                    channel.sendMessage(result).queue();
            }
            instance.setResult(result);
        }
    }
}
