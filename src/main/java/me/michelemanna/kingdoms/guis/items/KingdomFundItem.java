package me.michelemanna.kingdoms.guis.items;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.data.Kingdom;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.sql.Date;
import java.util.UUID;

public class KingdomFundItem extends AbstractItem {
    private final Kingdom kingdom;

    public KingdomFundItem(Kingdom kingdom) {
        this.kingdom = kingdom;
    }

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.GOLD_INGOT)
                .setDisplayName("§6Funds: " + kingdom.getFunds());
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        Economy economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
        double withdrawn = 0;
        long cooldown = 86400000;

        // check if the kingdom.lastFundsUpdate is not null and if the last update was less than 24 hours ago
        if (kingdom.getLastFundsUpdate() != null && System.currentTimeMillis() - kingdom.getLastFundsUpdate().getTime() < cooldown) {
            player.sendMessage(KingdomsPlugin.getInstance().getMessage("guis.kingdom-funds.cooldown"));
            return;
        }
        System.out.println(kingdom.getLastFundsUpdate());
        for (UUID uuid : kingdom.getMembers()) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

            if (!economy.has(offlinePlayer, KingdomsPlugin.getInstance().getConfig().getDouble("kingdom.funds-amount", 0))) {
                continue;
            }

            EconomyResponse response = economy.withdrawPlayer(offlinePlayer, KingdomsPlugin.getInstance().getConfig().getDouble("kingdom.funds-amount", 0));

            if (response.transactionSuccess()) {
                withdrawn += response.amount;

                if (offlinePlayer.isOnline()) {
                    offlinePlayer.getPlayer().sendMessage(KingdomsPlugin.getInstance().getMessage("guis.kingdom-funds.withdraw")
                            .replace("%amount%", String.valueOf((int) response.amount)));
                }
            }
        }

        kingdom.setLastFundsUpdate(new Date(System.currentTimeMillis()));
        kingdom.setFunds(kingdom.getFunds() + (int) withdrawn);

        KingdomsPlugin.getInstance().getDatabase().updateKingdom(kingdom);

        player.sendMessage(KingdomsPlugin.getInstance().getMessage("guis.kingdom-funds.success")
                .replace("%amount%", String.valueOf((int) withdrawn)));
    }
}