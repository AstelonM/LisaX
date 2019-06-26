package com.lisadevelopment.lisa.commands;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.ExecutionInstance;

public class Currency extends Command
{
    public Currency(ChatListener listener) {
        super(listener);
        name = "currency";
        description = "Get the value of a currency or convert between currencies";
        usage = listener.getPrefix() + "value <amount> <currency> to <currency2>";
        aliases = new String[] { "money", "value", "$" };
        examples = listener.getPrefix() + "value 30 usd to inr";
    }
    @Override
    public void treat(ExecutionInstance instance) {

    }
}
