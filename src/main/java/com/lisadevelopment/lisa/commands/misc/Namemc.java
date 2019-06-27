package com.lisadevelopment.lisa.commands.misc;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.ExecutionInstance;
import com.lisadevelopment.lisa.commands.Command;

public class Namemc extends Command { //TODO someone finish this
    Namemc(ChatListener listener) {
        super(listener);
        name = "namemc";
        description = "Get details about a Minecraft account.";
        usage = listener.getPrefix() + "namemc <username>";
        aliases = new String[] { "nmc", "minecraft" };
        examples = listener.getPrefix() + "namemc Astelon";
    }
    @Override
    public void treat(ExecutionInstance instance) {
        sendMessage(instance, "**Skin and name changes for " + instance.getText().split(" ")[1] + ":**");
    }
}
