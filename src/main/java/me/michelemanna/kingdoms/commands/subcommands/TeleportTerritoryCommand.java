package me.michelemanna.kingdoms.commands.subcommands;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.commands.SubCommand;
import me.michelemanna.kingdoms.data.Territory;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class TeleportTerritoryCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission("kingdoms.teleport")) {
            player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.no-permission"));
            return;
        }

        Map<Integer, List<Territory>> territories = KingdomsPlugin.getInstance().getTerritoryManager().getTerritories();

        if (territories.isEmpty()) {
            player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.teleport-territory.no-territories"));
            return;
        }

        Territory closestTerritory = null;
        double closestDistance = Double.MAX_VALUE;

        for (List<Territory> territoryList : territories.values()) {
            for (Territory territory : territoryList) {
                double distance = player.getLocation().distance(territory.getLocation(player.getWorld()));
                if (distance < closestDistance) {
                    closestTerritory = territory;
                    closestDistance = distance;
                }
            }
        }

        if (closestTerritory == null) {
            player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.teleport-territory.no-territories"));
            return;
        }

        player.teleport(closestTerritory.getLocation(player.getWorld()));

        player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.teleport-territory.success"));
    }
}
