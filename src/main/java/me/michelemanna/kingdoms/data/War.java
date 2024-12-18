package me.michelemanna.kingdoms.data;

import me.michelemanna.kingdoms.KingdomsPlugin;
import org.bukkit.Bukkit;
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
    private final BossBar bossBar = Bukkit.createBossBar(KingdomsPlugin.getInstance().getMessage("messages.managers.bossbar.name"), BarColor.RED, BarStyle.SOLID);
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

        members.forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);

            if (player != null) {
                bossBar.addPlayer(player);
            }
        });

        final Instant startTime = Instant.now();
        final int warTime = KingdomsPlugin.getInstance().getConfig().getInt("war-time");

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

        int attackerDeaths = (int) deaths.stream().filter(attacker.getMembers()::contains).count();
        int defenderDeaths = (int) deaths.stream().filter(defender.getMembers()::contains).count();

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

        KingdomsPlugin.getInstance().getDatabase().transferMembers(loser.getName(), winner.getName());
        KingdomsPlugin.getInstance().getDatabase().transferChunks(loser.getName(), winner.getName());

        Player winnerPlayer = Bukkit.getPlayer(winner.getLeaderId());
        Player loserPlayer = Bukkit.getPlayer(loser.getLeaderId());

        if (winnerPlayer != null) {
            winnerPlayer.sendTitle(KingdomsPlugin.getInstance().getMessage("messages.managers.war.title-success"), KingdomsPlugin.getInstance().getMessage("messages.managers.subtitle-success").replace("%name%", loser.getName()), 10 , 30, 10);
        }

        if (loserPlayer != null) {
            loserPlayer.sendTitle(KingdomsPlugin.getInstance().getMessage("messages.managers.war.title-failure"), KingdomsPlugin.getInstance().getMessage("messages.managers.subtitle-failure").replace("%name%", winner.getName()), 10 , 30, 10);
        }
    }
}