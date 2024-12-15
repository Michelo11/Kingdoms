package me.michelemanna.kingdoms.commands.subcommands;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.commands.SubCommand;
import me.michelemanna.kingdoms.guis.KingdomMenu;
import org.bukkit.entity.Player;

public class MenuKingdomCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission("kingdom.menu")) {
            player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.no-permission"));
            return;
        }

        new KingdomMenu().open(player);
    }
}