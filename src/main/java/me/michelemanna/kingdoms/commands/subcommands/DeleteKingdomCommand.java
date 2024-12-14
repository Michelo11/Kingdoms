package me.michelemanna.kingdoms.commands.subcommands;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.commands.SubCommand;
import org.bukkit.entity.Player;

public class DeleteKingdomCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission("kingdoms.delete")) {
            player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.no-permission"));
            return;
        }

        if (args.length != 2) {
            player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.delete-kingdom.usage"));
            return;
        }

        String kingdomName = args[1];

        KingdomsPlugin.getInstance().getDatabase().getKingdom(player.getUniqueId()).thenAccept(kingdom -> {
            if (kingdom == null) {
                player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.delete-kingdom.not-found"));
                return;
            }

            if (!kingdom.getLeaderId().equals(player.getUniqueId())) {
                player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.delete-kingdom.not-owner"));
                return;
            }

            KingdomsPlugin.getInstance().getDatabase().deleteKingdom(kingdomName).thenAccept(deleted -> {
                if (deleted) {
                    player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.delete-kingdom.success").replace("%name%", kingdomName));
                } else {
                    player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.delete-kingdom.error"));
                }
            });
        });
    }
}
