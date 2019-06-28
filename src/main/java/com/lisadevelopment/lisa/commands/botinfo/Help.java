package com.lisadevelopment.lisa.commands.botinfo;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.ExecutionInstance;
import com.lisadevelopment.lisa.commands.Command;
import com.lisadevelopment.lisa.commands.Flag;
import net.dv8tion.jda.api.entities.User;

public class Help extends Command {

    public Help(ChatListener listener) {
        super(listener);
        name = "help";
        description = "Get help with a command, or the list of commands and command groups.";
        usage = getPrefix() + "help ((command name)(" + listener.getFlagSeparator() + "flag name))";
        flags = new Flag[] {
                listener.getDeleteFlag(),
                listener.getChainingFlag(),
                listener.getIgnoreFlag()
        };
    }

    @Override
    public void treat(ExecutionInstance instance) {
        String text = instance.getText();
        User author = instance.getAuthor();
        if (!text.contains(" ")) {
            //TODO what to show in case of //help
        } else {
            text = text.substring(text.indexOf(" ")).trim();
            String[] components = text.split(listener.getFlagSeparator(), 2);
            Command com = listener.findCommand(components[0]);
            if (com == null) {
                sendMessage(instance, author.getAsMention() + " invalid command name.");
                return;
            }
            if (components.length == 1) {
                sendMessage(instance, com.toHelpFormat(text));
            } else {
                Flag flag = com.findFlag(components[1]);
                if (flag == null) {
                    sendMessage(instance, author.getAsMention() + " invalid flag name (" +
                            components[1] + ").");
                    return;
                }
                sendMessage(instance, flag.toHelpFormat());
            }
        }
    }
}
