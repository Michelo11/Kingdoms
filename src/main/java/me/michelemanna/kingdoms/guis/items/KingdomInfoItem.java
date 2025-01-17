package me.michelemanna.kingdoms.guis.items;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.conversations.RenameKingdomConversation;
import me.michelemanna.kingdoms.data.Kingdom;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class KingdomInfoItem extends AbstractItem {
    private final Kingdom kingdom;

    public KingdomInfoItem(Kingdom kingdom) {
        this.kingdom = kingdom;
    }

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.BOOK)
                .setDisplayName("§6Name: " + kingdom.getName());
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        player.closeInventory();

        RenameKingdomConversation conversation = new RenameKingdomConversation(kingdom);

        new ConversationFactory(KingdomsPlugin.getInstance())
                .withEscapeSequence("cancel")
                .withFirstPrompt(conversation)
                .withModality(false)
                .withLocalEcho(false)
                .buildConversation(player)
                .begin();
    }
}
