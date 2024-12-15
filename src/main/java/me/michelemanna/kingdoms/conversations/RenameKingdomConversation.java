package me.michelemanna.kingdoms.conversations;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.data.Kingdom;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RenameKingdomConversation extends StringPrompt {
    private final Kingdom kingdom;

    public RenameKingdomConversation(Kingdom kingdom) {
        this.kingdom = kingdom;
    }

    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
        return KingdomsPlugin.getInstance().getMessage("conversations.rename-kingdom.prompt");
    }

    @Override
    public @Nullable Prompt acceptInput(@NotNull ConversationContext context, @Nullable String input) {
        if (input == null) return this;

        try {
            kingdom.setName(input);

            KingdomsPlugin.getInstance().getDatabase().updateKingdom(kingdom);
        } catch (Exception e) {
            context.getForWhom().sendRawMessage(KingdomsPlugin.getInstance().getMessage("conversations.rename-kingdom.error"));
            e.printStackTrace();
            return END_OF_CONVERSATION;
        }

        context.getForWhom().sendRawMessage(KingdomsPlugin.getInstance().getMessage("conversations.rename-kingdom.success").replace("%name%", input));

        return END_OF_CONVERSATION;
    }
}
