package com.lisadevelopment.lisa.commands.management;

import com.lisadevelopment.lisa.ChatListener;
import com.lisadevelopment.lisa.ExecutionInstance;
import com.lisadevelopment.lisa.commands.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.Region;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.managers.GuildManager;

public class ChangeServerRegion extends Command {

    private ExecutionInstance instance;
    private String[] args;

    ChangeServerRegion(ChatListener listener){
        super(listener);
        name = "changeServerRegion";
        description = "Change the server Voice Channel region.";
        usage = getPrefix() + "changeServerRegion <region>";
        examples = getPrefix() + "changeServerRegion NorthAmerica";
        aliases = new String[] {"csr", "changeRegion", "serverRegion"};
    }

    @Override
    public void treat(ExecutionInstance i) {
        instance = i;
        Member member = instance.getGuild().getMember(instance.getAuthor());
        if (member == null || !member.hasPermission(Permission.MANAGE_SERVER)) {
            sendMessage(instance, "❌ | You cannot do this.");
            return;
        } else if (instance.getMessage().getContentRaw().split("\\s").length == 1) {
            sendMessage(instance,"❌ | Not enough arguments. `"+getUsage()+"`");
            return;
        }
        args = instance.getMessage().getContentRaw().split("\\s"); // Starts at 1 as 0 is the command
        String[] newArgs = new String[]{args[0],args[1].toLowerCase()};
        if(args.length == 3)
            newArgs = new String[]{args[0],args[1].toLowerCase()+"-"+args[2].toLowerCase()};
        instance = i;
        Region region = Region.fromKey(newArgs[1]);
        if(badRegion(region)) return;
        GuildManager manager = instance.getGuild().getManager();
        manager.setRegion(region).queue();
        sendMessage(instance,"✅ | Successfully changed server region to `"+region+"`");
    }

    private boolean badRegion(Region region){
        if(region == Region.UNKNOWN){
            sendMessage(instance,"❌ | Region not found: `"+instance.getMessage().getContentRaw().split("\\s")[1]+"`\n**Available regions:** `amsterdam, brazil, eu central, eu west, frankfurt, hongkong, japan, london, russia, singapore, southafrica, sydney, us central, us west, us east, us south`");
            return true;
        }
        return false;
    }
}
