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

        int x = player.getLocation().getChunk().getX();
        int z = player.getLocation().getChunk().getZ();

        KingdomsPlugin.getInstance().getDatabase().getTerritory(x, z).thenAccept(territory -> {
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
                        KingdomsPlugin.getInstance().getDatabase().createTerritory(kingdomName, x, z);
                        KingdomsPlugin.getInstance().getDatabase().addMember(kingdomName, player.getUniqueId());

                        player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.create-kingdom.success").replace("%name%", kingdomName));
                    } else {
                        player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.create-kingdom.error"));
                    }
                });
            });
        });
    }
}