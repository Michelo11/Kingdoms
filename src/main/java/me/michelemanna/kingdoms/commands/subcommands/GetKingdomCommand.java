package me.michelemanna.kingdoms.commands.subcommands;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.commands.SubCommand;
import org.bukkit.entity.Player;

public class GetKingdomCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission("kingdoms.get")) {
            player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.no-permission"));
            return;
        }

        KingdomsPlugin.getInstance().getDatabase().getKingdom(player.getUniqueId()).thenAccept(kingdom -> {
            if (kingdom == null) {
                player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.get-kingdom.no-kingdom"));
                return;
            }

            player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.get-kingdom.success")
                    .replace("%name%", kingdom.getName())
                    .replace("%level%", String.valueOf(kingdom.getLevel()))
                    .replace("%funds%", String.valueOf(kingdom.getFunds()))
                    .replace("%experience%", String.valueOf(kingdom.getExperience()))
            );
        });
    }
}
