package me.michelemanna.kingdoms.commands.subcommands;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.commands.SubCommand;
import me.michelemanna.kingdoms.conversations.InviteMemberConversation;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;

public class MemberKingdomCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission("kingdoms.member")) {
            player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.no-permission"));
            return;
        }

        if (args.length < 2) {
            player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.member-kingdom.usage"));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.player-not-found"));
            return;
        }

        InviteMemberConversation conversation = new InviteMemberConversation(player);

        new ConversationFactory(KingdomsPlugin.getInstance())
                .withEscapeSequence("cancel")
                .withFirstPrompt(conversation)
                .withModality(false)
                .withLocalEcho(false)
                .buildConversation(target)
                .begin();

        player.sendMessage(KingdomsPlugin.getInstance().getMessage("commands.member-kingdom.invite-sent").replace("%player%", target.getName()));
    }
}
