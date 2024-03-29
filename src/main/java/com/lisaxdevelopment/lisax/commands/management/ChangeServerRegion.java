package com.lisaxdevelopment.lisax.commands.management;

import com.lisaxdevelopment.lisax.ChatListener;
import com.lisaxdevelopment.lisax.ExecutionInstance;
import com.lisaxdevelopment.lisax.commands.Command;
import com.lisaxdevelopment.lisax.commands.Flag;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.Region;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.managers.GuildManager;

public class ChangeServerRegion extends Command {

    public ChangeServerRegion(ChatListener listener) {
        super(listener);
        name = "changeServerRegion";
        description = "Change the server Voice Channel region.";
        usage = getPrefix() + "changeServerRegion <region>";
        examples = getPrefix() + "changeServerRegion NorthAmerica";
        aliases = new String[]{"csr", "changeRegion", "serverRegion"};
        flags = new Flag[] {
                listener.getDeleteFlag(),
                listener.getChainingFlag(),
                listener.getIgnoreFlag(),
                listener.getSilentFlag()
        };
    }

    @Override
    public void treat(ExecutionInstance instance) {
        Member member = instance.getGuild().getMember(instance.getAuthor());
        if (member == null || !member.hasPermission(Permission.MANAGE_SERVER)) {
            sendMessage(instance, "❌ | You cannot do this.");
            return;
        } else if (instance.getMessage().getContentRaw().split("\\s").length == 1) {
            sendMessage(instance, "❌ | Not enough arguments. `" + getUsage() + "`");
            return;
        }
        String[] args = instance.getMessage().getContentRaw().split("\\s"); // Starts at 1 as 0 is the command
        String[] newArgs = new String[]{args[0], args[1].toLowerCase()};
        if (args.length == 3)
            newArgs = new String[]{args[0], args[1].toLowerCase() + "-" + args[2].toLowerCase()};
        Region region = Region.fromKey(newArgs[1]);
        if (badRegion(region, instance)) return;
        GuildManager manager = instance.getGuild().getManager();
        manager.setRegion(region).queue();
        sendMessage(instance, "✅ | Successfully changed server region to `" + region + "`");
    }

    private boolean badRegion(Region region, ExecutionInstance instance) {
        if (region == Region.UNKNOWN) {
            sendMessage(instance, "❌ | Region not found: `" + instance.getMessage().getContentRaw()
                    .split("\\s")[1] + "`\n**Available regions:** " +
                    "`amsterdam, brazil, eu central, eu west, frankfurt, hongkong, japan, london, russia, singapore, " +
                    "southafrica, sydney, us central, us west, us east, us south`");
            return true;
        }
        return false;
    }
}
