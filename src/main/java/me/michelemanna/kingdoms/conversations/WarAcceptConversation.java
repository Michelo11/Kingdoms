package me.michelemanna.kingdoms.conversations;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.data.Kingdom;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WarAcceptConversation extends StringPrompt {
    private final Kingdom attacker;

    public WarAcceptConversation(Kingdom attacker) {
        this.attacker = attacker;
    }

    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext context) {
        return KingdomsPlugin.getInstance().getMessage("conversations.war-accept.prompt").replace("%name%", attacker.getName());
    }

    @Override
    public @Nullable Prompt acceptInput(@NotNull ConversationContext context, @Nullable String input) {
        if (input == null) return this;

        KingdomsPlugin.getInstance().getDatabase().getKingdom(((Player) context.getForWhom()).getUniqueId()).thenAccept(defender -> {
            Player player = Bukkit.getPlayer(attacker.getLeaderId());


            if (input.equalsIgnoreCase("yes")) {
                if (player != null) player.sendMessage(KingdomsPlugin.getInstance().getMessage("conversations.war-accept.success-attacker").replace("%name%", defender.getName()));

                context.getForWhom().sendRawMessage(KingdomsPlugin.getInstance().getMessage("conversations.war-accept.success").replace("%name%", attacker.getName()));

                KingdomsPlugin.getInstance().getWarManager().startWar(attacker, defender);
            } else if (input.equalsIgnoreCase("no")) {
                if (player != null) player.sendMessage(KingdomsPlugin.getInstance().getMessage("conversations.war-accept.no-war-attacker").replace("%name%", defender.getName()));

                context.getForWhom().sendRawMessage(KingdomsPlugin.getInstance().getMessage("conversations.war-accept.no-war").replace("%name%", attacker.getName()));
            } else {
                context.getForWhom().sendRawMessage(KingdomsPlugin.getInstance().getMessage("conversations.war-accept.invalid"));
            }
        });

        return END_OF_CONVERSATION;
    }
}
