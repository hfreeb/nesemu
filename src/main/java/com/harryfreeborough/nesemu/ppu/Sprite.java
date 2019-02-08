package com.harryfreeborough.nesemu.ppu;

/**
 * Holds data of a sprite which will be rendered on the next scanline.
 *
 * <p>Note: Pooled to reduce strain on GC so this is mutable.</p>
 */
public class Sprite {

    private int pattern;
    private int priority;
    private int x;
    private int oamIndex;

    /**
     * Returns the colour for each of the 8 pixels in the next scanline row as 4 bits each (32-bits of data).
     *
     * @return colour data for
     */
    public int getPattern() {
        return this.pattern;
    }

    public void setPattern(int pattern) {
        this.pattern = pattern;
    }

    /**
     * Returns the sprite priority, that is, whether it renders in front or behind the background.
     * (0: in front of background; 1: behind background)
     *
     * @return priority of the sprite
     */
    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * Returns the left most pixel's x value.
     *
     * @return x position
     */
    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    /**
     * Returns the index of the data for this sprite in the OAM.
     *
     * @return oam index
     */
    public int getOamIndex() {
        return this.oamIndex;
    }

    public void setOamIndex(int oamIndex) {
        this.oamIndex = oamIndex;
    }

}
