package com.github.rogueTetrisLicencjat;

import com.badlogic.gdx.utils.Array;

public class MapNode {
    public enum Type { ENEMY, ELITE, REST, MYSTERY, BOSS }
    private Type type;
    private float x, y;
    private boolean completed = false;
    private boolean reachable = false;
    private Array<MapNode> connections = new Array<>();

    public MapNode(Type type, float x, float y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }

    public float getX() { return x; }
    public void setX(float x) { this.x = x; }

    public float getY() { return y; }
    public void setY(float y) { this.y = y; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public boolean isReachable() { return reachable; }
    public void setReachable(boolean reachable) { this.reachable = reachable; }

    public boolean isBoss() { return type == Type.BOSS; }

    public Array<MapNode> getConnections() { return connections; }
}
