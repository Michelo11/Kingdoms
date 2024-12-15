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

        KingdomsPlugin.getInstance().getDatabase().getKingdom(player.getUniqueId()).thenAccept(kingdom -> {
            if (kingdom == null) {
                player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.delete-kingdom.not-found"));
                return;
            }

            KingdomsPlugin.getInstance().getDatabase().deleteKingdom(kingdom.getName()).thenAccept(deleted -> {
                if (deleted) {
                    KingdomsPlugin.getInstance().getDatabase().deleteTerritories(kingdom.getName());
                    KingdomsPlugin.getInstance().getDatabase().removeMembers(kingdom.getName());

                    player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.delete-kingdom.success").replace("%name%", kingdom.getName()));
                } else {
                    player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.delete-kingdom.error"));
                }
            });
        });
    }
}
