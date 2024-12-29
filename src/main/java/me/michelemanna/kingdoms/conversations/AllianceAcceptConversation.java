package me.michelemanna.kingdoms.conversations;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.data.Kingdom;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class AllianceAcceptConversation extends StringPrompt {
    private final Kingdom sender;

    public AllianceAcceptConversation(Kingdom sender) {
        this.sender = sender;
    }

    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext context) {
        return KingdomsPlugin.getInstance().getMessage("conversations.alliance-accept.prompt").replace("%name%", sender.getName());
    }

    @Override
    public @Nullable Prompt acceptInput(@NotNull ConversationContext context, @Nullable String input) {
        if (input == null) return this;

        KingdomsPlugin.getInstance().getDatabase().getKingdom(((Player) context.getForWhom()).getUniqueId()).thenAccept(ally -> {
            Player player = Bukkit.getPlayer(sender.getLeaderId());


            if (input.equalsIgnoreCase("yes")) {
                if (player != null) {
                    player.sendTitle(KingdomsPlugin.getInstance().getMessage("conversations.alliance-accept.title-success"), KingdomsPlugin.getInstance().getMessage("conversations.alliance-accept.subtitle-success-attacker").replace("%name%", sender.getName()), 10, 30, 10);
                    player.playSound(player, Sound.BLOCK_ANVIL_FALL, 1, 1);
                }

                ((Player) context.getForWhom()).sendTitle(KingdomsPlugin.getInstance().getMessage("conversations.alliance-accept.title-success"), KingdomsPlugin.getInstance().getMessage("conversations.alliance-accept.subtitle-success").replace("%name%", ally.getName()), 10, 30, 10);
                ((Player) context.getForWhom()).playSound(((Player) context.getForWhom()), Sound.BLOCK_ANVIL_FALL, 1, 1);

                KingdomsPlugin.getInstance().getWarManager().getWars(sender).forEach(war -> {
                    war.getAlliances().add(ally);

                    for (UUID uuid : ally.getMembers()) {
                        Player member = Bukkit.getPlayer(uuid);

                        if (member != null) {
                            war.getBossBar().addPlayer(member);
                        }
                    }

                    war.getBossBar().addPlayer((Player) context.getForWhom());
                });
            } else if (input.equalsIgnoreCase("no")) {
                if (player != null) player.sendMessage(KingdomsPlugin.getInstance().getMessage("conversations.alliance-accept.no-alliance-ally").replace("%name%", sender.getName()));

                context.getForWhom().sendRawMessage(KingdomsPlugin.getInstance().getMessage("conversations.alliance-accept.no-alliance").replace("%name%", ally.getName()));
            } else {
                context.getForWhom().sendRawMessage(KingdomsPlugin.getInstance().getMessage("conversations.alliance-accept.invalid"));
            }
        });

        return END_OF_CONVERSATION;
    }
}
