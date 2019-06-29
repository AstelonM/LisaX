package com.lisaxdevelopment.lisax.commands.botinfo;

import com.lisaxdevelopment.lisax.ChatListener;
import com.lisaxdevelopment.lisax.ExecutionInstance;
import com.lisaxdevelopment.lisax.LisaX;
import com.lisaxdevelopment.lisax.commands.Command;
import com.lisaxdevelopment.lisax.commands.Flag;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.time.Instant;

public class Help extends Command {

    Help(ChatListener listener) {
        super(listener);
        name = "help";
        usage = getPrefix() + "help ((command name)(" + listener.getFlagSeparator() + "flag name))";
        examples = getPrefix() + "help\n" + getPrefix() + "help list\n" + getPrefix() + "help list" +
                listener.getFlagSeparator() + "del";
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
            sendMessage(instance, new EmbedBuilder()
                    .setAuthor("LisaX", null, listener.getJda().getSelfUser().getEffectiveAvatarUrl())
                    .setColor(LisaX.config.getBotColor())
                    .setTimestamp(Instant.now())
                    .setThumbnail(listener.getJda().getSelfUser().getEffectiveAvatarUrl())
                    .setDescription("**LisaX** is a bot made for Discord Hack-Week 2019. The name comes from the 5 " +
                            "developers that worked on it: Lava, Ibu, Slateyo, Astelon and Xuxe. It has a variety of " +
                            "utilities for discord and more. Type in **" + getPrefix() + "list** for the list of commands.\n" +
                            "You can use " + getPrefix() + "help (commandName) to get more information about a command." +
                            "The usage of a command follows the following format: () marks optional arguments, while <> " +
                            "marks required arguments.\nOn top of that, LisaX stands out for two special features: " +
                            "command flags and command chaining.")
                    .addField("Flags", "Flags represent special text that can be appended to the command " +
                            "name, separated by **" + listener.getFlagSeparator() + "**, that allow changing the " +
                            "behaviour of commands without needing a whole new command. The bot has currently 4 \"global\" " +
                            "flags which are found on most commands:\n-ignore (ig), which makes the bot ignore the " +
                            "command (even if it's chained)\n-delete (del), which makes the bot delete the command\n" +
                            "-silent (s), which makes the bot not send any response\n-chained (ch), which tells the bot " +
                            "that it should look for commands inside the input\n\nIt is possible for a command to have " +
                            "custom flags. You can see what flags a command has using " + getPrefix() + "help (commandName). " +
                            "You can get more information about a flag using " + getPrefix() + "help (commandName-flagName).\n",
                            false)
                    .addField("Chaining", "Chaining represents the bot's ability to execute chained commands; " +
                            "that is, the bot can execute multiple commands in the same message if the first one has the " +
                            "chaining flag, from right to left. Each command will use the result of the previously " +
                            "executed one, if existent. Usually, the commands that have a result are those that return " +
                            "plain text to the user. Nevertheless, nothing stops the user from chaining commands that " +
                            "don't return a result. The input for a particular chained command can be stopped early using **" +
                            getPrefix() + "-**. This counts as a command of its own, so make sure there's space after it",
                            false)
                    .addField("Other features", "Apart from these, LisaX supports the following features:\n" +
                            "**Name enforcing** - servers that require certain effective names for members can enable this " +
                            "feature to stop users without a nickname from changing their effective name (by giving them " +
                            "a nickname)\n**Public roles** - the bot can set up roles that can be taken by any member" +
                            "\n**Notes** - Take personal notes. /shrug", false)
                    .build());
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
