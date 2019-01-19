package com.harryfreeborough.nesemu.ppu;

public class Ppu {
    
    private final PpuState state;
    private final PpuMemory memory;
    
    public Ppu(PpuState state, PpuMemory memory) {
        this.state = state;
        this.memory = memory;
    }
    
    public int readRegister(int address) {
        switch (address) {
            case 0x2002:
                int value = this.state.register;
                value |= this.state.flagSpriteOverflow << 5;
                value |= this.state.flagSpriteZeroHit << 6;
                value |= (this.state.flagNmiOccurred ? 1 : 0) << 7;
                this.state.flagNmiOccurred = false;
                return value;
            case 0x2004:
                break;
            case 0x2007:
                break;
        }
        
        return 0;
    }
    
    public void writeRegister(int address, int value) {
        this.state.register = value & 0x1F;
        switch (address) {
            case 0x2000:
                this.state.flagNametable = value & 0x03;
                this.state.flagAddressIncrement = (value >> 2) & 0x01;
                this.state.flagPatternTable = (value >> 3) & 0x01;
                this.state.flagBackgroundTable = (value >> 4) & 0x01;
                this.state.flagSpriteSize = (value >> 5) & 0x01;
                this.state.flagMasterSlave = (value >> 6) & 0x01;
                this.state.flagNmiOutput = (value >> 7) == 0x01;
                break;
            case 0x2001:
                break;
            case 0x2003:
                break;
            case 0x2004:
                break;
            case 0x2005:
                break;
            case 0x2006:
                break;
            case 0x2007:
                break;
        }
    }
    
    public void runScanline() {
        if (this.state.scanline == -1) { //Pre-render scanline
            this.state.flagNmiOccurred = false;
        } else if (this.state.scanline < 240) { //Visible scanlines
        } else if (this.state.scanline == 240) { //Post-render scanline
        } else if (this.state.scanline == 241){ //Vertical blanking started
            this.state.flagNmiOccurred = true;
        }
        
        this.state.scanline++;
        if (this.state.scanline > 260) {
            this.state.scanline = -1;
        }
    }
    
}
