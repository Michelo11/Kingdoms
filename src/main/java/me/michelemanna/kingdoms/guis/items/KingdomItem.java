package me.michelemanna.kingdoms.guis.items;

import me.michelemanna.kingdoms.data.Kingdom;
import org.bukkit.Material;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;

public class KingdomItem extends SimpleItem {
    public KingdomItem(Kingdom kingdom) {
        super(new ItemBuilder(Material.BEACON)
                .setDisplayName("§6Name: " + kingdom.getName()));
    }
}
