package com.harryfreeborough.nesemu.ppu;

public class PpuState {

    //$2000: PPUCTRL
    private int flagNametable; //0 = $2000; 1 = $2400; 2 = $2800; 3 = $2C00
    private int flagAddressIncrement; //0 = add 1; 1 = add 32
    private int flagPatternTable; //0 = $0000; 1 = $1000
    private int flagBackgroundTable; //0 = $0000; 1 = $1000
    private int flagSpriteSize; //0 = 8x8 pixels; 1 = 8x16 pixels
    private int flagMasterSlave; //0 = read from EXT; 1 = write to EXT
    private boolean flagNmiOutput;

    //$2002: PPUSTATUS
}
