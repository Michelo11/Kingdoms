package me.michelemanna.kingdoms.data;

public class Territory {
    private final int x;
    private final int z;
    private final boolean isProtected;
    private final long captured_at;

    public Territory(int x, int z, boolean isProtected, long captured_at) {
        this.x = x;
        this.z = z;
        this.isProtected = isProtected;
        this.captured_at = captured_at;
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

    public long getCapturedAt() {
        return captured_at;
    }
}