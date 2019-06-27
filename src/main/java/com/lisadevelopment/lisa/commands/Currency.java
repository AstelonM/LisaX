package com.lisadevelopment.lisa.commands;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.Config;
import com.lisadevelopment.lisa.ExecutionInstance;
import org.jsoup.Jsoup;

import java.io.IOException;

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
    public static String getCurrencyJSON(String current)  {
        try {
            return Jsoup.connect("http://data.fixer.io/api/latest?access_key="+ Config.fixerKey).ignoreContentType(true).execute().body();
        }catch(IOException e)
        {
            return current;
        }
    }
}
