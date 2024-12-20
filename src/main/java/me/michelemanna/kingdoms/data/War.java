package me.michelemanna.kingdoms.data;

import me.michelemanna.kingdoms.KingdomsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class War {
    private final Kingdom attacker;
    private final Kingdom defender;
    private final List<UUID> deaths = new ArrayList<>();
    private final BossBar bossBar = Bukkit.createBossBar(KingdomsPlugin.getInstance().getMessage("managers.bossbar.name"), BarColor.RED, BarStyle.SOLID);
    private BukkitTask task;

    public War(Kingdom attacker, Kingdom defender) {
        this.attacker = attacker;
        this.defender = defender;
    }

    public Kingdom getAttacker() {
        return attacker;
    }

    public List<UUID> getDeaths() {
        return deaths;
    }

    public Kingdom getDefender() {
        return defender;
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    public void start() {
        List<UUID> members = new ArrayList<>();

        members.addAll(attacker.getMembers());
        members.addAll(defender.getMembers());
        members.add(attacker.getLeaderId());
        members.add(defender.getLeaderId());

        members.forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);

            if (player != null) {
                bossBar.addPlayer(player);
            }
        });

        final Instant startTime = Instant.now();
        final int warTime = KingdomsPlugin.getInstance().getConfig().getInt("kingdom.war-time", 300);

        task = new BukkitRunnable() {
            @Override
            public void run() {
                Instant now = Instant.now();
                long seconds = now.getEpochSecond() - startTime.getEpochSecond();
                long remaining = warTime - seconds;

                if (remaining <= 0) {
                    end();
                    return;
                }

                bossBar.setProgress((double) remaining / warTime);
            }
        }.runTaskTimer(KingdomsPlugin.getInstance(), 0, 20L);
    }

    public void end() {
        bossBar.removeAll();
        task.cancel();
        KingdomsPlugin.getInstance().getWarManager().endWar(this);

        int attackerDeaths = (int) deaths.stream().filter(uuid -> attacker.getMembers().contains(uuid) || attacker.getLeaderId().equals(uuid)).count();
        int defenderDeaths = (int) deaths.stream().filter(uuid -> defender.getMembers().contains(uuid) || defender.getLeaderId().equals(uuid)).count();

        Kingdom winner = null;
        Kingdom loser = null;

        if (attackerDeaths > defenderDeaths) {
            winner = defender;
            loser = attacker;
        } else if (defenderDeaths > attackerDeaths) {
            winner = attacker;
            loser = defender;
        }

        if (winner == null) return;

        KingdomsPlugin.getInstance().getKingdomManager().addExperience(winner, KingdomsPlugin.getInstance().getConfig().getInt("kingdom.war-experience", 100));
        KingdomsPlugin.getInstance().getDatabase().transferMembers(loser.getName(), winner.getName());
        KingdomsPlugin.getInstance().getDatabase().transferChunks(loser.getName(), winner.getName());

        Player winnerPlayer = Bukkit.getPlayer(winner.getLeaderId());
        Player loserPlayer = Bukkit.getPlayer(loser.getLeaderId());

        if (winnerPlayer != null) {
            winnerPlayer.sendTitle(KingdomsPlugin.getInstance().getMessage("managers.war.title-success"), KingdomsPlugin.getInstance().getMessage("managers.war.subtitle-success").replace("%name%", loser.getName()), 10 , 30, 10);
            winnerPlayer.playSound(winnerPlayer, Sound.ENTITY_ENDER_DRAGON_DEATH, 1, 1);
            winnerPlayer.sendMessage(KingdomsPlugin.getInstance().getMessage("managers.war.experience").replace("%experience%", String.valueOf(KingdomsPlugin.getInstance().getConfig().getInt("kingdom.war-experience"))));
        }

        if (loserPlayer != null) {
            loserPlayer.sendTitle(KingdomsPlugin.getInstance().getMessage("managers.war.title-failure"), KingdomsPlugin.getInstance().getMessage("managers.war.subtitle-failure").replace("%name%", winner.getName()), 10 , 30, 10);
            loserPlayer.playSound(loserPlayer, Sound.ENTITY_ENDER_DRAGON_DEATH, 1, 1);
        }
    }
}