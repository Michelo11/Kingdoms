package me.michelemanna.kingdoms.guis.items;

import me.michelemanna.kingdoms.data.Kingdom;
import org.bukkit.Material;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;

public class KingdomLevelItem extends SimpleItem {
    public KingdomLevelItem(Kingdom kingdom) {
        super(new ItemBuilder(Material.ENDER_EYE)
                .setDisplayName("§6Level: " + kingdom.getLevel()));
    }
}