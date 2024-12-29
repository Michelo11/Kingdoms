package me.michelemanna.kingdoms.guis.items;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.conversations.AllianceConfirmConversation;
import me.michelemanna.kingdoms.conversations.WarConfirmConversation;
import me.michelemanna.kingdoms.data.Kingdom;
import me.michelemanna.kingdoms.data.War;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
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
        OfflinePlayer player = Bukkit.getOfflinePlayer(kingdom.getLeaderId());

        return new ItemBuilder(Material.BEACON)
                .setDisplayName("§6Name: " + kingdom.getName())
                .setLegacyLore(List.of("§6Level: " + kingdom.getLevel(), "§6Leader: " + player.getName()));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        player.closeInventory();

        switch (clickType) {
            case LEFT:
                if (kingdom.getLeaderId().equals(player.getUniqueId())) {
                    player.sendMessage(KingdomsPlugin.getInstance().getMessage("guis.cant-war-yourself"));
                    return;
                }

                WarConfirmConversation warConversation = new WarConfirmConversation(kingdom);

                new ConversationFactory(KingdomsPlugin.getInstance())
                        .withEscapeSequence("cancel")
                        .withFirstPrompt(warConversation)
                        .withModality(false)
                        .withLocalEcho(false)
                        .buildConversation(player)
                        .begin();

                break;
            case RIGHT:
                if (kingdom.getLeaderId().equals(player.getUniqueId())) {
                    player.sendMessage(KingdomsPlugin.getInstance().getMessage("guis.cant-ally-yourself"));
                    return;
                }

                War war = KingdomsPlugin.getInstance().getWarManager().getWar(player.getUniqueId());

                if (war == null) {
                    player.sendMessage(KingdomsPlugin.getInstance().getMessage("guis.no-war"));
                    return;
                }

                AllianceConfirmConversation allianceConversation = new AllianceConfirmConversation(kingdom);

                new ConversationFactory(KingdomsPlugin.getInstance())
                        .withEscapeSequence("cancel")
                        .withFirstPrompt(allianceConversation)
                        .withModality(false)
                        .withLocalEcho(false)
                        .buildConversation(player)
                        .begin();

                break;
        }
    }
}
