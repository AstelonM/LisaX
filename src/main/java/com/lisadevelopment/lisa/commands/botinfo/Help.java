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
