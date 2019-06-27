package com.lisadevelopment.lisa.commands.management;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.ExecutionInstance;
import com.lisadevelopment.lisa.commands.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.Region;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.managers.GuildManager;

public class ChangeServerRegion extends Command {
    ChangeServerRegion(ChatListener listener){
        super(listener);
        name = "changeServerRegion";
        description = "Change the server Voice Channel region.";
        usage = getPrefix() + "changeServerRegion <region>";
        examples = getPrefix() + "changeServerRegion NorthAmerica";
        aliases = new String[] {"csr", "changeRegion", "serverRegion"};
    }

    @Override
    public void treat(ExecutionInstance instance) {
        Member member = instance.getGuild().getMember(instance.getAuthor());
        if (member == null || !member.hasPermission(Permission.MANAGE_SERVER)) {
            sendMessage(instance, "❌ | You cannot do this.");
            return;
        } else if (instance.getMessage().getContentRaw().split("\\s").length == 1) {
            sendMessage(instance,"❌ | Not enough arguments. `"+getUsage()+"`");
            return;
        }
        Region region = Region.fromKey(formatRegion(instance.getMessage().getContentRaw()));
        GuildManager manager = instance.getGuild().getManager();
        manager.setRegion(region).queue();
    }

    private String formatRegion(String region) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for(String str : region.split("\\s")){
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
