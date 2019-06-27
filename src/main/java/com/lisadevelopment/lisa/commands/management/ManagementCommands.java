package com.lisadevelopment.lisa.commands.management;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.ExecutionInstance;
import com.lisadevelopment.lisa.commands.CommandGroup;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ManagementCommands extends CommandGroup {
    public ManagementCommands(ChatListener listener) {
        super(listener);
        name = "ManagementCommands";
        description = "Commands for managing users of a guild.";
        addCommand(new GiveRole(listener));
        addCommand(new TakeRole(listener));
        addCommand(new ChangeServerRegion(listener));
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
