package com.lisadevelopment.lisa.commands.user;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.ExecutionInstance;
import com.lisadevelopment.lisa.commands.Command;
import com.lisadevelopment.lisa.commands.Flag;
import com.lisadevelopment.lisa.utils.Format;
import com.lisadevelopment.lisa.utils.GetFromString;
import com.lisadevelopment.lisa.utils.StringUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public class Nickname extends Command {

    public Nickname(ChatListener listener) {
        super(listener);
        name = "nickname";
        description = "Set the nickname of yourself or another user.";
        usage = getPrefix() + "nickname (user mention/name/id) <nickname>";
        aliases = new String[] { "nick" };
        examples = getPrefix() + "nick\n" + getPrefix() + "nick My new nickname\n" + getPrefix() + "nick Astelon NotAstelon";
        flags = new Flag[] {
                listener.getSilentFlag(),
                listener.getDeleteFlag(),
                listener.getChainingFlag(),
                listener.getIgnoreFlag()
        };
    }

    @Override
    public void treat(ExecutionInstance instance) {
        Guild guild = instance.getGuild();
        Member author = instance.getMember();
        String text = instance.getText();
        Member bot = guild.getSelfMember();
        Member target;
        String newNick = null;
        if (!text.contains(" ")) {
            target = author;
        } else {
            text = text.substring(text.indexOf(" ")).trim();
            String targetText = StringUtils.firstWord(text);
            Member possibleTarget = GetFromString.getMember(guild, targetText, Format.MENTION, Format.NAME, Format.ID);
            if (possibleTarget == null) {
                target = author;
                newNick = text;
            } else {
                target = possibleTarget;
                newNick = text.substring(targetText.length()).trim();
            }
        }
        boolean targetBot = false;
        boolean targetAuthor = false;
        if (target.equals(bot)) {
            if (!author.hasPermission(Permission.NICKNAME_MANAGE)) {
                sendMessage(instance, author.getAsMention() + " you don't have permission to manage " +
                        "nicknames, how do you want to change mine? :P");
                return;
            } else if (author.canInteract(target)) {
                sendMessage(instance, author.getAsMention() + " I'm above you in the role hierarchy, " +
                        "you can't change my nickname :P");
                return;
            } else if (!bot.hasPermission(Permission.NICKNAME_CHANGE)) {
                sendMessage(instance, author.getAsMention() + " I don't have permission to change my " +
                        "my nickname, you'll have to do it yourself.");
                return;
            }
            targetBot = true;
        } else if (target.equals(author)) {
            if (!author.hasPermission(Permission.NICKNAME_CHANGE)) {
                sendMessage(instance, author.getAsMention() + " you don't have permission to change your " +
                        "nickname. I won't help you do it >_>");
                return;
            } else if (!bot.hasPermission(Permission.NICKNAME_MANAGE)) {
                sendMessage(instance, author.getAsMention() + " I don't have permission to change nicknames. " +
                        "You can change yours on your own though.");
                return;
            } else if (!bot.canInteract(author)) {
                sendMessage(instance, author.getAsMention() + " you are above me in role hierarchy, I can't " +
                        "change your nickname.");
                return;
            }
            targetAuthor = true;
        } else {
            if (!author.hasPermission(Permission.NICKNAME_MANAGE)) {
                sendMessage(instance, author.getAsMention() + " you don't have permission to change " +
                        "nicknames, I won't help you with it.");
                return;
            } else if (!author.canInteract(target)) {
                sendMessage(instance, author.getAsMention() + " " + target.getEffectiveName() + " is above " +
                        "you in the role hierarchy, you can't change his nickname.");
                return;
            } else if (!bot.hasPermission(Permission.NICKNAME_MANAGE)) {
                sendMessage(instance, author.getAsMention() + " I don't have permission to change " +
                        "nicknames, but you do.");
                return;
            } else if (!bot.canInteract(target)) {
                sendMessage(instance, author.getAsMention() + " " + target.getEffectiveName() + " is above " +
                        "me in the role hierarchy, I can't change his nickname.");
                return;
            }
        }
        if (newNick != null && newNick.length() > 32) {
            sendMessage(instance, author.getAsMention() + " nicknames can't be longer than 32 characters.");
            return;
        }
        if (newNick == null && target.getNickname() == null || newNick != null && newNick.equals(target.getNickname())) {
            sendMessage(instance, author.getAsMention() + " the old nickname is the same as the new one, " +
                    "consider it changed :P");
            return;
        }
        boolean finalTargetBot = targetBot;
        boolean finalTargetAuthor = targetAuthor;
        try {
            guild.modifyNickname(target, newNick).queue(
                    (ignore) -> {
                        if (finalTargetBot)
                            sendMessage(instance, author.getAsMention() + " my nickname was changed successfully.");
                        else if (finalTargetAuthor)
                            sendMessage(instance, author.getAsMention() + " enjoy your new nickname :P");
                        else
                            sendMessage(instance, target.getAsMention() + " your nickname was changed " +
                                    "by " + author.getEffectiveName() + ".");
                    },
                    (exception) -> sendMessage(instance, author.getAsMention() + " I couldn't change the nickname.")
            );
        } catch (Exception e) {
            sendMessage(instance, author.getAsMention() + " I couldn't change the nickname.");
        }
    }
}
