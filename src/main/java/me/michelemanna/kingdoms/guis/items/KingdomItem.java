package me.michelemanna.kingdoms.guis.items;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.conversations.WarConfirmConversation;
import me.michelemanna.kingdoms.data.Kingdom;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.util.List;

public class KingdomItem extends AbstractItem {
    private final Kingdom kingdom;

    public KingdomItem(Kingdom kingdom) {
        this.kingdom = kingdom;
    }

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.BEACON)
                .setDisplayName("§6Name: " + kingdom.getName())
                .setLegacyLore(List.of("§6Level: " + kingdom.getLevel()));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        player.closeInventory();

        if (kingdom.getLeaderId().equals(player.getUniqueId())) {
            player.sendMessage(KingdomsPlugin.getInstance().getMessage("guis.cant_war_yourself"));
            return;
        }

        WarConfirmConversation conversation = new WarConfirmConversation(kingdom);

        new ConversationFactory(KingdomsPlugin.getInstance())
                .withEscapeSequence("cancel")
                .withFirstPrompt(conversation)
                .withModality(false)
                .withLocalEcho(false)
                .buildConversation(player)
                .begin();
    }
}
