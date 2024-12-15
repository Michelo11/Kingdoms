package me.michelemanna.kingdoms.data;

public class Territory {
    private final int x;
    private final int z;
    private final boolean isProtected;

    public Territory(int x, int z, boolean isProtected) {
        this.x = x;
        this.z = z;
        this.isProtected = isProtected;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public boolean isProtected() {
        return isProtected;
    }
}