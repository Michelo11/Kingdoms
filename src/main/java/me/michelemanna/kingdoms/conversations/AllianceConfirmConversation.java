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

public class AllianceConfirmConversation extends StringPrompt {
    private final Kingdom ally;

    public AllianceConfirmConversation(Kingdom ally) {
        this.ally = ally;
    }

    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext context) {
        return KingdomsPlugin.getInstance().getMessage("conversations.alliance-confirm.prompt").replace("%name%", ally.getName());
    }

    @Override
    public @Nullable Prompt acceptInput(@NotNull ConversationContext context, @Nullable String input) {
        if (input == null) return this;

        Player player = Bukkit.getPlayer(ally.getLeaderId());

        if (input.equalsIgnoreCase("yes")) {
            KingdomsPlugin.getInstance().getDatabase().getKingdom(((Player) context.getForWhom()).getUniqueId()).thenAccept(kingdom -> {
                if (player != null) {
                    AllianceAcceptConversation conversation = new AllianceAcceptConversation(kingdom);

                    new ConversationFactory(KingdomsPlugin.getInstance())
                            .withEscapeSequence("cancel")
                            .withFirstPrompt(conversation)
                            .withModality(false)
                            .withLocalEcho(false)
                            .buildConversation(player)
                            .begin();
                }
            });

            context.getForWhom().sendRawMessage(KingdomsPlugin.getInstance().getMessage("conversations.alliance-confirm.success").replace("%name%", ally.getName()));
        } else if (input.equalsIgnoreCase("no")) {
            context.getForWhom().sendRawMessage(KingdomsPlugin.getInstance().getMessage("conversations.alliance-confirm.no-alliance"));
        } else {
            context.getForWhom().sendRawMessage(KingdomsPlugin.getInstance().getMessage("conversations.alliance-confirm.invalid"));
        }

        return END_OF_CONVERSATION;
    }
}
