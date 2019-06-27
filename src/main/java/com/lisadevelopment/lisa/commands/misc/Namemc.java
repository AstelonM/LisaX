package com.lisadevelopment.lisa.commands.misc;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.ExecutionInstance;
import com.lisadevelopment.lisa.commands.Command;

public class Namemc extends Command {
    Namemc(ChatListener listener) {
        super(listener);
        name = "currency";
        description = "Get the value of a currency or convert between currencies";
        usage = listener.getPrefix() + "value <amount> <currency> to <currency2>";
        aliases = new String[] { "money", "value", "$" };
        examples = listener.getPrefix() + "value 30 usd to inr";
    }
    @Override
    public void treat(ExecutionInstance instance) {
        sendMessage(instance, "ok");
    }
}
