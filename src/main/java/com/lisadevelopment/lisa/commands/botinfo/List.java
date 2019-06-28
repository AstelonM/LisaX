package com.lisadevelopment.lisa.commands.botinfo;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.Config;
import com.lisadevelopment.lisa.ExecutionInstance;
import com.lisadevelopment.lisa.commands.Command;
import com.lisadevelopment.lisa.commands.CommandGroup;
import com.lisadevelopment.lisa.commands.Flag;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

public class List extends Command {

    public List(ChatListener listener) {
        super(listener);
        name = "list";
        description = "Get the list of commands and command groups registered to the chat listener, or those belonging " +
                "to a command group.";
        usage = getPrefix() + "list (command group name)";
        aliases = new String[] { "commands",  "commandList" };
        examples = getPrefix() + "list\n" + getPrefix() + "list BotInfoCommands";
        flags = new Flag[] {
                listener.getDeleteFlag(),
                listener.getChainingFlag(),
                listener.getIgnoreFlag()
        };
    }

    private MessageEmbed getCommandList() {
        EmbedBuilder result = new EmbedBuilder()
                .setAuthor("Command List", null, listener.getJda().getSelfUser().getEffectiveAvatarUrl())
                .setColor(Config.BOT_COLOR)
                .setFooter("For more help, use " + getPrefix() + "help <command name/alias>", null);
        ArrayList<MessageEmbed.Field> fields = listener.formatCommandList("");
        for (MessageEmbed.Field field: fields)
            result.addField(field);
        return result.build();
    }

    @Override
    public void treat(ExecutionInstance instance) {
        String text = instance.getText();
        User author = instance.getAuthor();
        if (!text.contains(" ")) {
            if (instance.isShouldReply()) {
                author.openPrivateChannel().queue(
                        privateChannel ->  {
                            sendMessage(instance, privateChannel, getCommandList());
                            sendMessage(instance, author.getAsMention() + " the command list was sent to your pms.");
                        });
            }
        } else {
            text = text.substring(text.indexOf(" ")).trim();
            CommandGroup result = getListener().findCommandGroup(text);
            if (result == null)
                sendMessage(instance, "Invalid command group name, " + author.getAsMention() + ".");
            else {
                sendMessage(instance, result.formatGroupCommands());
            }
        }
    }
}
