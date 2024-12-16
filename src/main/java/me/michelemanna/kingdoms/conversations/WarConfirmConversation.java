package me.michelemanna.kingdoms.conversations;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.data.Kingdom;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WarConfirmConversation extends StringPrompt {
    private final Kingdom defender;

    public WarConfirmConversation(Kingdom defender) {
        this.defender = defender;
    }

    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext context) {
        return KingdomsPlugin.getInstance().getMessage("conversations.war-confirm.prompt").replace("%name%", defender.getName());
    }

    @Override
    public @Nullable Prompt acceptInput(@NotNull ConversationContext context, @Nullable String input) {
        if (input == null) return this;
        Player player = Bukkit.getPlayer(defender.getLeaderId());

        if (input.equalsIgnoreCase("yes")) {
            KingdomsPlugin.getInstance().getDatabase().getKingdom(((Player) context.getForWhom()).getUniqueId()).thenAccept(kingdom -> {
                if (player != null) {
                    WarAcceptConversation conversation = new WarAcceptConversation(kingdom);

                    new ConversationFactory(KingdomsPlugin.getInstance())
                            .withEscapeSequence("cancel")
                            .withFirstPrompt(conversation)
                            .withModality(false)
                            .withLocalEcho(false)
                            .buildConversation(player)
                            .begin();
                }
            });

            context.getForWhom().sendRawMessage(KingdomsPlugin.getInstance().getMessage("conversations.war-confirm.success").replace("%name%", defender.getName()));
        } else if (input.equalsIgnoreCase("no")) {
            context.getForWhom().sendRawMessage(KingdomsPlugin.getInstance().getMessage("conversations.war-confirm.no-war"));
        } else {
            context.getForWhom().sendRawMessage(KingdomsPlugin.getInstance().getMessage("conversations.war-confirm.invalid"));
        }

        return END_OF_CONVERSATION;
    }
}