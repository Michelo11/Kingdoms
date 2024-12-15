package me.michelemanna.kingdoms.guis.items;

import me.michelemanna.kingdoms.data.Kingdom;
import org.bukkit.Material;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;

public class KingdomExperienceItem extends SimpleItem {
    public KingdomExperienceItem(Kingdom kingdom) {
        super(new ItemBuilder(Material.EXPERIENCE_BOTTLE)
                .setDisplayName("§6Experience: " + kingdom.getExperience()));
    }
}