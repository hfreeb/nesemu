package com.harryfreeborough.nesemu.ppu;

public class PpuState {
    
    public final byte[] nametableData = new byte[0x1000];
    public final byte[] palleteData = new byte[0x20];
    
    //The least 5 significant bits of the value last written into a register
    public int register;
    
    public int scanline = -1; //Current scanline being processed
    
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
    
    //$2002: PPUSTATUS
    public int flagSpriteOverflow;
    public int flagSpriteZeroHit;
    public boolean flagNmiOccurred;
    
}
