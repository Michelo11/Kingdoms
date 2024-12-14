package me.michelemanna.kingdoms.commands.subcommands;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.commands.SubCommand;
import org.bukkit.entity.Player;

public class ListKingdomCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission("kingdoms.list")) {
            player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.no-permission"));
            return;
        }

        KingdomsPlugin.getInstance().getDatabase().getKingdoms().thenAccept(kingdoms -> {
            if (kingdoms.isEmpty()) {
                player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.list-kingdom.no-kingdoms"));
                return;
            }

            kingdoms.forEach(kingdom -> player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.list-kingdoms.success")
                    .replace("%name%", kingdom.getName())
            ));
        });
    }
}