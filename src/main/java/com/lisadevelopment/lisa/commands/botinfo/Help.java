package com.lisadevelopment.lisa.commands.botinfo;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.Config;
import com.lisadevelopment.lisa.ExecutionInstance;
import com.lisadevelopment.lisa.commands.Command;
import com.lisadevelopment.lisa.commands.Flag;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.time.Instant;

public class Help extends Command {

    public Help(ChatListener listener) {
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
/*
        private MessageEmbed getMemberInfo(Member member) { //TODO
        String roleString = member.getRoles().stream().map(IMentionable::getAsMention)
                .collect(Collectors.joining(", "));
        String permissionString = member.getPermissions().stream().map(Permission::getName)
                .collect(Collectors.joining(", "));
        return new EmbedBuilder()
                .setAuthor("LisaX", null, user.getEffectiveAvatarUrl())//change
                .setColor(member.getColor())
                .setTimestamp(Instant.now())
                .setThumbnail(user.getEffectiveAvatarUrl())//change
                .setDescription("**LisaX** is a bot made for Discord Hack-Week 2019. The name is the initial letters of the usernames of the developers that worked on the bot It has a variety of commands all based around productivity. Type in `//list` for the list of commands. The command behavior can be customized using flags using the format: `//commandName-flag1-flag2`. You can use the `-ap` flag at the first command to run multiple commands in the same line, from right to left, with each new command using the result of the previous one in its input. The list of flags is `-s, -ig, -del/-d, -ap/-ch/-appending` which stand for `silent, ignore, delete, appending` respectively.")
                .build();
    }
    */
    @Override
    public void treat(ExecutionInstance instance) {
        String text = instance.getText();
        User author = instance.getAuthor();
        if (!text.contains(" ")) {
            sendMessage(instance, new EmbedBuilder()
                    .setAuthor("LisaX", null, listener.getJda().getSelfUser().getEffectiveAvatarUrl())
                    .setColor(Config.BOT_COLOR)
                    .setTimestamp(Instant.now())
                    .setThumbnail(listener.getJda().getSelfUser().getEffectiveAvatarUrl())
                    .setDescription("**LisaX** is a bot made for Discord Hack-Week 2019. The name comes from the 5 " +
                            "developers that worked on it: Lava, Ibu, Slateyo, Astelon and Xuxe. It has a variety of " +
                            "utilities for discord and more. Type in **" + getPrefix() + "list** for the list of commands.\n" +
                            "On top of that, LisaX stands out for two special features: command flags and command chaining.")
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
                            "a nickname)", false)
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
