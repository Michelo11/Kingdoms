package me.michelemanna.kingdoms.commands;

import org.bukkit.entity.Player;

public interface SubCommand {
    void execute(Player player, String[] args);
}