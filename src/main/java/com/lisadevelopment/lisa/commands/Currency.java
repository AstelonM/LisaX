package com.lisadevelopment.lisa.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.Config;
import com.lisadevelopment.lisa.ExecutionInstance;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.Date;

public class Currency extends Command
{
    public Currency(ChatListener listener) {
        super(listener);
        this.name = "currency";
        this.description = "Get the value of a currency or convert between currencies";
        this.usage = listener.getPrefix() + "value <amount> <currency> to <currency2>";
        this.aliases = new String[]{"money", "value", "$", "cur"};
        this.examples = listener.getPrefix() + "value 30 usd to inr";
    }
    @Override
    public void treat(ExecutionInstance instance) {
        if (Config.lastUpdatedCurrency.getDate() != Date.from(Instant.now()).getDate()) {
            Config.currencyJSON = (JsonObject) new JsonParser().parse(Currency.getCurrencyJSON(Config.currencyJSON.toString()));
            Config.lastUpdatedCurrency = Date.from(Instant.now());
        }
        MessageChannel channel = instance.getChannel();
        double amount;
        String currencyFrom;
        String currencyTo;
        try {
            String[] args = instance.getText().substring(instance.getText().indexOf(' ') + 1).split(" ");
            if (args.length == 0) {
                throw new Exception("Usage: `" + this.usage + "`");
            }
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
            double result = parseJSON(amount, currencyFrom.toUpperCase(), currencyTo.toUpperCase());
            DecimalFormat df = new DecimalFormat("###.##");
            channel.sendMessage(df.format(amount) + " **" + currencyFrom.toUpperCase() + "** = " + df.format(result) + " **" + currencyTo.toUpperCase() + "**").queue();
        }
        catch (Exception e)
        {
            channel.sendMessage("Something went wrong. `" + e.getMessage() + "`").queue();
            e.printStackTrace();
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

    private double parseJSON(double amount, String currencyFrom, String currencyTo) {
        JsonObject rates = Config.currencyJSON.getAsJsonObject("rates");
        //get rates in EUR
        double fromEUR = rates.get(currencyFrom.substring(0, 3)).getAsDouble();
        double toEUR = rates.get(currencyTo.substring(0, 3)).getAsDouble();
        return (toEUR / fromEUR) * amount;
    }
}
