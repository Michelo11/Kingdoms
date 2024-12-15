package me.michelemanna.kingdoms.guis.items;

import me.michelemanna.kingdoms.data.Kingdom;
import org.bukkit.Material;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;

public class KingdomFundItem extends SimpleItem {
    public KingdomFundItem(Kingdom kingdom) {
        super(new ItemBuilder(Material.GOLD_INGOT)
                .setDisplayName("§6Funds: " + kingdom.getFunds()));
    }
}