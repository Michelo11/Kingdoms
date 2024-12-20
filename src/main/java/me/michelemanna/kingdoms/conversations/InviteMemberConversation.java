package me.michelemanna.kingdoms.conversations;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.data.Kingdom;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InviteMemberConversation extends StringPrompt {
    private final Player player;

    public InviteMemberConversation(Player player) {
        this.player = player;
    }


    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
        return KingdomsPlugin.getInstance().getMessage("conversations.invite-member.prompt").replace("%player%", player.getName());
    }

    @Override
    public @Nullable Prompt acceptInput(@NotNull ConversationContext context, @Nullable String input) {
        if (input == null) return this;

        Player target = (Player) context.getForWhom();
        Kingdom kingdomTarget = KingdomsPlugin.getInstance().getKingdomManager().getKingdom(target);

        if (kingdomTarget != null) {
            player.sendMessage(KingdomsPlugin.getInstance().getMessage("conversations.invite-member.already-member"));
            return END_OF_CONVERSATION;
        }

        Kingdom kingdom = KingdomsPlugin.getInstance().getKingdomManager().getKingdom(player);

        if (kingdom == null) {
            player.sendMessage(KingdomsPlugin.getInstance().getMessage("conversations.invite-member.no-kingdom"));
            return END_OF_CONVERSATION;
        }

        if (!kingdom.canAddMember()) {
            player.sendMessage(KingdomsPlugin.getInstance().getMessage("conversations.invite-member.max-members"));
            return END_OF_CONVERSATION;
        }

        KingdomsPlugin.getInstance().getDatabase().addMember(kingdom.getName(), target.getUniqueId());

        player.sendMessage(KingdomsPlugin.getInstance().getMessage("conversations.invite-member.success").replace("%player%", target.getName()));

        return END_OF_CONVERSATION;
    }
}
