package me.michelemanna.kingdoms.data;

public record Territory(int x, int z, boolean isProtected) {
    public boolean isNear(Territory territory) {
        return Math.abs(x - territory.x) <= 1 && Math.abs(z - territory.z) <= 1;
    }
}