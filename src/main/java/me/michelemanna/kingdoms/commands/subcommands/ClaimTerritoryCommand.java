package me.michelemanna.kingdoms.commands.subcommands;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.commands.SubCommand;
import org.bukkit.entity.Player;

public class ClaimTerritoryCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission("kingdoms.claim")) {
            player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.no-permission"));
            return;
        }

        int x = player.getLocation().getChunk().getX();
        int z = player.getLocation().getChunk().getZ();

        KingdomsPlugin.getInstance().getDatabase().getTerritory(x, z).thenAccept(territory -> {
            if (territory != null) {
                player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.claim-territory.already-claimed"));
                return;
            }

            KingdomsPlugin.getInstance().getDatabase().getKingdom(player.getUniqueId()).thenAccept(kingdom -> {
                if (kingdom == null) {
                    player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.claim-territory.no-kingdom"));
                    return;
                }

                KingdomsPlugin.getInstance().getDatabase().createTerritory(kingdom.getName(), x, z);

                player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.claim-territory.success"));
            });
        });
    }
}