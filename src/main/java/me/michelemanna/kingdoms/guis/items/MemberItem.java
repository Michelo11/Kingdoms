package me.michelemanna.kingdoms.guis.items;

import me.michelemanna.kingdoms.KingdomsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.builder.SkullBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import xyz.xenondevs.invui.util.MojangApiUtils;

import java.io.IOException;
import java.util.UUID;

public class MemberItem extends AbstractItem {
    private final UUID id;

    public MemberItem(UUID id) {
        this.id = id;
    }

    @Override
    public ItemProvider getItemProvider() {
        try {
            return new SkullBuilder(id)
                    .setDisplayName("§6Name: " + Bukkit.getOfflinePlayer(id).getName());
        } catch (MojangApiUtils.MojangApiException | IOException e) {
            return new ItemBuilder(Material.PLAYER_HEAD)
                    .setDisplayName("§6Name: " + id);
        }
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        if (clickType == ClickType.DROP && !player.getUniqueId().equals(id)) {
            KingdomsPlugin.getInstance().getDatabase().removeMember(id);

            player.sendMessage(KingdomsPlugin.getInstance().getMessage("guis.remove-member.success"));
        } else {
            player.closeInventory();
            player.sendMessage(KingdomsPlugin.getInstance().getMessage("guis.remove-member.error"));
        }
    }
}