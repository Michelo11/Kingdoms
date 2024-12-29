package me.michelemanna.kingdoms.data;

import org.bukkit.Location;
import org.bukkit.World;

public record Territory(int x, int z) {
    public boolean isNear(Territory territory) {
        return Math.abs(x - territory.x) <= 1 && Math.abs(z - territory.z) <= 1;
    }

    public Location getLocation(World world) {
        for (int y = world.getMaxHeight() - 1; y > 0; y--) {
            Location location = world.getChunkAt(x, z).getBlock(8, y, 8).getLocation();
            if (!location.getBlock().isEmpty()) {
                return location.add(0, 1, 0);
            }
        }

        return null;
    }
}