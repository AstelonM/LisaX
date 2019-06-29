package com.lisaxdevelopment.lisax.commands.user;

import com.lisaxdevelopment.lisax.ChatListener;
import com.lisaxdevelopment.lisax.ExecutionInstance;
import com.lisaxdevelopment.lisax.commands.CommandGroup;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class UserCommands extends CommandGroup {

    public UserCommands(ChatListener listener) {
        super(listener);
        name = "UserCommands";
        description = "User related commands.";
        addCommand(new Avatar(listener));
        addCommand(new GetDiscriminator(listener));
        addCommand(new GetEffectiveName(listener));
        addCommand(new GetNickname(listener));
        addCommand(new GetUserId(listener));
        addCommand(new GetUsername(listener));
        addCommand(new GetRoles(listener));
        addCommand(new Nickname(listener));
        addCommand(new UserInfo(listener));
        addCommand(new Note(listener));
    }

    @Override
    public void execute(MessageReceivedEvent event, String command) {
        if (!event.isFromGuild()) {
            event.getChannel().sendMessage("This command is restricted to guilds, so you have to do it there.").queue();
            return;
        }
        super.execute(event, command);
    }

    @Override
    public boolean treatHeader(ExecutionInstance instance) {
        if (instance.getGuild() == null) {
            instance.getChannel().sendMessage("This command is restricted to guilds, so you have to do it there.").queue();
            return false;
        }
        return super.treatHeader(instance);
    }

    @Override
    public void treat(ExecutionInstance instance) {
        if (instance.getGuild() == null) {
            instance.getChannel().sendMessage("This command is restricted to guilds, so you have to do it there.").queue();
            return;
        }
        super.treat(instance);
    }
}
