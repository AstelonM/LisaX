package com.lisadevelopment.lisa.commands.management;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.ExecutionInstance;
import com.lisadevelopment.lisa.commands.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.Region;
import net.dv8tion.jda.api.managers.GuildManager;

public class ChangeServerRegion extends Command {

    private ExecutionInstance instance;

    ChangeServerRegion(ChatListener listener){
        super(listener);
        name = "changeServerRegion";
        description = "Change the server Voice Channel region.";
        usage = getPrefix() + "changeServerRegion <region>";
        examples = getPrefix() + "changeServerRegion NorthAmerica";
        aliases = new String[] {"csr", "changeRegion", "serverRegion"};
    }

    //TODO understand why Member#hasPermission is producing a NullPointerException

    @Override
    public void treat(ExecutionInstance i){
        if(!instance.getGuild().getMember(instance.getAuthor()).hasPermission(Permission.MANAGE_SERVER) || !instance.getGuild().getMember(instance.getAuthor()).hasPermission(Permission.ADMINISTRATOR)){
            sendMessage(instance, "❌ | You cannot do this.");
            return;
        }
        if(instance.getMessage().getContentRaw().split("\\s").length == 1){
            sendMessage(instance,"❌ | Not enough arguments. `"+getUsage()+"`");
            return;
        }
        instance = i;
        Region region = Region.fromKey(formatRegion());
        GuildManager manager = instance.getGuild().getManager();
        manager.setRegion(region).queue();
    }

    private String formatRegion(){
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for(String str : instance.getMessage().getContentRaw().split("\\s")){
            if(i++ == 0) continue;
            else if(i-1 == 2){
                sb.append(str);
                continue;
            }
            sb.append(str).append("_");
        }
        return sb.toString().trim();
    }
}
