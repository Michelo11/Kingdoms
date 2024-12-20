package me.michelemanna.kingdoms.commands.subcommands;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.commands.SubCommand;
import me.michelemanna.kingdoms.guis.LevelMenu;
import org.bukkit.entity.Player;

public class LevelKingdomCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission("kingdoms.level")) {
            player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.no-permission"));
            return;
        }

        new LevelMenu().open(player);
    }
}