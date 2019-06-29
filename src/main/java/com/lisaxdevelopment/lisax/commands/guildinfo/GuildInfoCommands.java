package com.lisaxdevelopment.lisax.commands.guildinfo;

import com.lisaxdevelopment.lisax.ChatListener;
import com.lisaxdevelopment.lisax.ExecutionInstance;
import com.lisaxdevelopment.lisax.commands.CommandGroup;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class GuildInfoCommands extends CommandGroup {

    public GuildInfoCommands(ChatListener listener) {
        super(listener);
        name = "GuildInfoCommands";
        description = "Commands for getting info about a guild.";
        addCommand(new CountMembersByRole(listener));
        addCommand(new EmojiImage(listener));
        addCommand(new EmojiList(listener));
        addCommand(new GetColor(listener));
        addCommand(new GetPermissions(listener));
        addCommand(new RoleId(listener));
        addCommand(new RoleMembers(listener));
        addCommand(new GuildInfo(listener));
        addCommand(new FirstMessage(listener));
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
