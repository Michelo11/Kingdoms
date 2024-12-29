package me.michelemanna.kingdoms.commands;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.commands.subcommands.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KingdomCommand implements TabExecutor {
    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public KingdomCommand() {
        this.subCommands.put("create", new CreateKingdomCommand());
        this.subCommands.put("menu", new MenuKingdomCommand());
        this.subCommands.put("list", new ListKingdomCommand());
        this.subCommands.put("delete", new DeleteKingdomCommand());
        this.subCommands.put("claim", new ClaimTerritoryCommand());
        this.subCommands.put("unclaim", new UnclaimTerritoryCommand());
        this.subCommands.put("member", new MemberKingdomCommand());
        this.subCommands.put("level", new LevelKingdomCommand());
        this.subCommands.put("teleport", new TeleportTerritoryCommand());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.player-only"));
            return true;
        }

        if (args.length == 0) {
            // TODO: Show help
            return true;
        }

        if (subCommands.containsKey(args[0].toLowerCase())) {
            subCommands.get(args[0].toLowerCase()).execute((Player) sender, args);
            return true;
        }

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("member")) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        }

        return new ArrayList<>(this.subCommands.keySet());
    }
}