package com.lisadevelopment.lisa.commands.guildinfo;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.ExecutionInstance;
import com.lisadevelopment.lisa.commands.Command;
import com.lisadevelopment.lisa.commands.Flag;

public class GuildInfo extends Command {

    public GuildInfo(ChatListener listener) {
        super(listener);
        name = "guildInfo";
        description = "Get information about the guild.";
        usage = getPrefix() + "guildInfo";
        aliases = new String[] { "getGuildInfo", "guildInformation", "getGuildInformation", "serverInfo",
                "getServerInfo", "serverInformation", "getServerInformation" };
        examples = getPrefix() + "guildInfo";
        flags = new Flag[] {
                listener.getChainingFlag(),
                listener.getDeleteFlag(),
                listener.getIgnoreFlag()
        };
    }

    @Override
    public void treat(ExecutionInstance instance) { //TODO to implement
        
    }
}
