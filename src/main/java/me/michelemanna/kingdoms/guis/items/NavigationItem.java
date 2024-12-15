package me.michelemanna.kingdoms.guis.items;

import me.michelemanna.kingdoms.KingdomsPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.ControlItem;

public class NavigationItem extends ControlItem<PagedGui<Item>> {
    private final boolean next;

    public NavigationItem(boolean next) {
        this.next = next;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        if (next) getGui().goForward();
        else getGui().goBack();
    }

    @Override
    public ItemProvider getItemProvider(PagedGui<Item> itemPagedGui) {
        return new ItemBuilder(Material.ARROW)
                .setDisplayName(next ? KingdomsPlugin.getInstance().getMessage("guis.next-page") : KingdomsPlugin.getInstance().getMessage("guis.previous-page"));
    }
}
