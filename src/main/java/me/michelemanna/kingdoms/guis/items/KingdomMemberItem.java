package me.michelemanna.kingdoms.guis.items;

import me.michelemanna.kingdoms.data.Kingdom;
import me.michelemanna.kingdoms.guis.KingdomMenu;
import me.michelemanna.kingdoms.guis.MembersMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class KingdomMemberItem extends AbstractItem {
    private final Kingdom kingdom;

    public KingdomMemberItem(Kingdom kingdom) {
        this.kingdom = kingdom;
    }

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .setDisplayName("§6Members");
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        new MembersMenu(kingdom).open(player);
    }
}
