package me.michelemanna.kingdoms.commands.subcommands;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.commands.SubCommand;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

public class UnclaimTerritoryCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission("kingdoms.unclaim")) {
            player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.no-permission"));
            return;
        }

        Chunk chunk = player.getLocation().getChunk();

        Integer territoryKingdomId = KingdomsPlugin.getInstance().getTerritoryManager().getKingdom(chunk);

        if (territoryKingdomId == null) {
            player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.unclaim-territory.not-claimed"));
            return;
        }

        KingdomsPlugin.getInstance().getDatabase().getKingdom(player.getUniqueId()).thenAccept(kingdom -> {
            if (kingdom == null) {
                player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.unclaim-territory.no-kingdom"));
                return;
            }

            if (!territoryKingdomId.equals(kingdom.getId())) {
                player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.unclaim-territory.not-your-territory"));
                return;
            }

            if (KingdomsPlugin.getInstance().getTerritoryManager().getChunks(kingdom.getId()).size() == 1) {
                player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.unclaim-territory.last-territory"));
                return;
            }

            int x = chunk.getX();
            int z = chunk.getZ();

            KingdomsPlugin.getInstance().getDatabase().deleteTerritory(kingdom.getName(), x, z);

            player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.unclaim-territory.success"));
        });
    }
}
