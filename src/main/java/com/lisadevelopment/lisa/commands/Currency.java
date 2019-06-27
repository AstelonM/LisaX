package com.lisadevelopment.lisa.commands;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.Config;
import com.lisadevelopment.lisa.ExecutionInstance;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jsoup.Jsoup;

import java.io.IOException;

public class Currency extends Command
{
    public Currency(ChatListener listener) {
        super(listener);
        this.name = "currency";
        this.description = "Get the value of a currency or convert between currencies";
        this.usage = listener.getPrefix() + "value <amount> <currency> to <currency2>";
        this.aliases = new String[]{"money", "value", "$"};
        this.examples = listener.getPrefix() + "value 30 usd to inr";
    }
    @Override
    public void treat(ExecutionInstance instance) {
        MessageChannel channel = instance.getChannel();
        channel.sendTyping().queue();
        double amount = 0;
        String currencyFrom = "";
        String currencyTo = "USD";
        try {
            String[] args = instance.getText().substring(instance.getText().indexOf(' ') + 1).split(" ");
            if (Character.isDigit(args[0].charAt(0))) {
                switch (args.length)
                {
                    case 4: amount = Double.parseDouble(args[0]);
                            currencyFrom = args[1];
                            currencyTo = args[3];
                            break;
                    case 3: amount = Double.parseDouble(args[0]);
                            currencyFrom = args[1];
                            currencyTo = args[2];
                            break;
                    case 2: amount = Double.parseDouble(args[0]);
                            currencyFrom = args[1];
                            currencyTo = (args[1].equalsIgnoreCase("USD"))?"EUR":"USD";
                            break;
                    case 1: throw new Exception("Not enough arguments");
                    default:throw new Exception("Too many arguments");
                }
            }
            else
            {
                if(args.length==1)
                {
                    amount = 1;
                    currencyFrom = args[0];
                    currencyTo = (args[0].equalsIgnoreCase("USD"))?"EUR":"USD";
                }
                else
                {
                    throw new Exception("Usage: `"+this.usage+"`");
                }
            }
        }
        catch (Exception e)
        {
            channel.sendMessage("Something went wrong. +`"+e.getMessage()+"`").queue();
        }
    }

    public static String getCurrencyJSON(String current) {
        try {
            return Jsoup.connect("http://data.fixer.io/api/latest?access_key="+ Config.fixerKey).ignoreContentType(true).execute().body();
        } catch (IOException e)
        {
            return current;
        }
    }
}
