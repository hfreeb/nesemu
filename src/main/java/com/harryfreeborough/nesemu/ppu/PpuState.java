package com.harryfreeborough.nesemu.ppu;

public class PpuState {
    
    public final byte[] nametableData = new byte[0x1000];
    public final int[] palleteData = new int[0x20];
    public final int[] oamData = new int[0x100];

    //Not object as this is quite speed critical
    public final Sprite[] sprites = new Sprite[8];

    public final int[] backbuffer = new int[256 * 240];
    
    //The least 5 significant bits of the value last written into a register
    public int register;
    
    public int frame;
    public int scanline;
    public int dot;
    public int cycle; //Total dots across program session
    
    public boolean oddFrame;
    
    public int nametableByte;
    public int attribTableByte;
    public int lowTileByte;
    public int highTileByte;

    public long tileData;

    public int spriteCount;

    public int regOamAddr;
    
    public int regV; //Current VRAM address (15 bits)
    public int regT; //Temporary VRAM address (15 bits)
    public int regX; //Fine X scroll (3 bits)
    public int flagW; //First or second write toggle
    
    //$2000: PPUCTRL
    public int flagNametable; //0 = $2000; 1 = $2400; 2 = $2800; 3 = $2C00
    public int flagAddressIncrement; //0 = add 1; 1 = add 32
    public int flagPatternTable; //0 = $0000; 1 = $1000
    public int flagBackgroundTable; //0 = $0000; 1 = $1000
    public int flagSpriteSize; //0 = 8x8 pixels; 1 = 8x16 pixels
    public int flagMasterSlave; //0 = read from EXT; 1 = write to EXT
    public boolean flagNmiOutput;
    
    //$2001: PPUMASK
    public boolean flagGreyscale; //Produce a greyscale display
    public boolean flagLeftmostBackground; //Show background in leftmost 8 pixels
    public boolean flagLeftmostSprites; //Show sprites in leftmost 8 pixels
    public boolean flagBackground; //Show background
    public boolean flagSprites; //Show sprites
    public boolean flagEmphRed; //Emphasise reds
    public boolean flagEmphGreen; //Emphasise greens
    public boolean flagEmphBlue; //Emphasise blues
    
    //$2002: PPUSTATUS
    public boolean flagSpriteOverflow;
    public boolean flagSpriteZeroHit;
    public boolean flagNmiOccurred;
    
    public int dataBuffer;

    public PpuState() {
        for (int i = 0; i < this.sprites.length; i++) {
            this.sprites[i] = new Sprite();
        }
    }
    
}
