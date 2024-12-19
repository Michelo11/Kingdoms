package me.michelemanna.kingdoms.commands.subcommands;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MemberKingdomCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission("kingdoms.member")) {
            player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.no-permission"));
            return;
        }

        if (args.length < 2) {
            player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.member-kingdom.usage"));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.player-not-found"));
            return;
        }

        KingdomsPlugin.getInstance().getDatabase().getKingdom(player.getUniqueId()).thenAccept(kingdom -> {
            if (kingdom == null) {
                player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.member-kingdom.no-kingdom"));
                return;
            }

            if (!kingdom.canAddMember()) {
                player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.member-kingdom.max-members"));
                return;
            }

            KingdomsPlugin.getInstance().getDatabase().addMember(kingdom.getName(), target.getUniqueId());

            player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.member-kingdom.success").replace("%player%", target.getName()));
        });
    }
}
