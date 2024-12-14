package me.michelemanna.kingdoms.commands.subcommands;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.commands.SubCommand;
import org.bukkit.entity.Player;

public class CreateKingdomCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission("kingdoms.create")) {
            player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.no-permission"));
            return;
        }

        if (args.length != 2) {
            player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.create-kingdom.usage"));
            return;
        }

        String kingdomName = args[1];

        KingdomsPlugin.getInstance().getDatabase().getTerritory(player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ()).thenAccept(territory -> {
            if (territory != null) {
                player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.create-kingdom.territory-already-claimed"));
                return;
            }

            KingdomsPlugin.getInstance().getDatabase().getKingdom(player.getUniqueId()).thenAccept(kingdom -> {
                if (kingdom != null) {
                    player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.create-kingdom.already-have-kingdom"));
                    return;
                }

                KingdomsPlugin.getInstance().getDatabase().createKingdom(player.getUniqueId(), kingdomName).thenAccept(created -> {
                    if (created) {
                        player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.create-kingdom.success").replace("&name%", kingdomName));

                        KingdomsPlugin.getInstance().getDatabase().createTerritory(kingdomName, player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ());
                    } else {
                        player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.create-kingdom.error"));
                    }
                });
            });
        });
    }
}