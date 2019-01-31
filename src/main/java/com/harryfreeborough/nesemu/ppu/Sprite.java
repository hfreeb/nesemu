package com.harryfreeborough.nesemu.ppu;

//Reused/cached to reduce strain on GC
public class Sprite {

    private int pattern;
    private int priority;
    private int x;
    private int oamIndex;

    public int getPattern() {
        return this.pattern;
    }

    public void setPattern(int pattern) {
        this.pattern = pattern;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getOamIndex() {
        return this.oamIndex;
    }

    public void setOamIndex(int oamIndex) {
        this.oamIndex = oamIndex;
    }

}
