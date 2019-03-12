package com.harryfreeborough.nesemu.ppu;

/**
 * Holds the state that the {@link Ppu} has.
 */
public class PpuState {

    public final byte[] nametableData = new byte[0x1000];
    public final int[] palleteData = new int[0x20];
    public final int[] oamData = new int[0x100];

    //'Pooled' as this is quite speed critical (objects not recreated, but data set)
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

    public void copy(PpuState state) {
        System.arraycopy(state.nametableData, 0, this.nametableData, 0, state.nametableData.length);
        System.arraycopy(state.palleteData, 0, this.palleteData, 0, state.palleteData.length);
        System.arraycopy(state.oamData, 0, this.oamData, 0, state.oamData.length);

        this.frame = state.frame;
        this.register = state.register;
        this.oddFrame = state.oddFrame;
        this.regOamAddr = state.regOamAddr;
        this.regV = state.regV;
        this.regT = state.regT;
        this.regX = state.regX;
        this.flagW = state.flagW;

        this.flagNametable = state.flagNametable;
        this.flagAddressIncrement = state.flagAddressIncrement;
        this.flagPatternTable = state.flagPatternTable;
        this.flagBackgroundTable = state.flagBackgroundTable;
        this.flagSpriteSize = state.flagSpriteSize;
        this.flagMasterSlave = state.flagMasterSlave;
        this.flagNmiOutput = state.flagNmiOutput;

        this.flagGreyscale = state.flagGreyscale;
        this.flagLeftmostBackground = state.flagLeftmostBackground;
        this.flagLeftmostSprites = state.flagLeftmostSprites;
        this.flagBackground = state.flagBackground;
        this.flagSprites = state.flagSprites;
        this.flagEmphRed = state.flagEmphRed;
        this.flagEmphGreen = state.flagEmphGreen;
        this.flagEmphBlue = state.flagEmphBlue;
    }

    @Override
    public PpuState clone() {
        PpuState state = new PpuState();

        state.copy(this);

        return state;
    }

    public void reset() {
        copy(new PpuState());
    }

}
