package com.harryfreeborough.nesemu.ppu;

import com.harryfreeborough.nesemu.Console;
import com.harryfreeborough.nesemu.NesEmu;
import com.harryfreeborough.nesemu.cpu.CpuState;

import static com.harryfreeborough.nesemu.utils.MemoryUtils.bitPresent;
import static com.harryfreeborough.nesemu.utils.MemoryUtils.shiftBit;

public class Ppu {

    private final Console console;
    private final PpuState state;
    private final PpuMemory memory;

    public Ppu(Console console, PpuMemory memory) {
        this.console = console;
        this.memory = memory;

        this.state = new PpuState();
        reset();
    }

    public void reset() {
        writeRegister(0x2000, 0);
        writeRegister(0x2001, 0);
        writeRegister(0x2003, 0);
        this.state.regOamAddr = 0;
        this.state.frame = 0;
        this.state.scanline = 240;
        this.state.dot = 340;
    }

    public void renderPixel() {
        int x = this.state.dot - 1;
        int y = this.state.scanline;

        int background = 0;
        if (this.state.flagBackground && (x >= 8 || this.state.flagLeftmostBackground)) {
            int tileData = (int) (this.state.tileData >> 32);
            background = (tileData >> ((7 - this.state.regX) * 4)) & 0xF;
        }
    
        if (background >= 16 && background % 4 == 0) {
            background -= 16;
        }
        
        this.state.backbuffer[y * 256 + x] = this.console.getPpu().getState().palleteData[background] % 64;
    }

    public int readRegister(int address) {
        switch (address) {
            case 0x2002: {
                int value = this.state.register;
                value |= shiftBit(this.state.flagSpriteOverflow, 5);
                value |= shiftBit(this.state.flagSpriteZeroHit, 6);
                value |= shiftBit(this.state.flagNmiOccurred, 7);
                this.state.flagNmiOccurred = false;
                this.state.flagW = 0;
                return value;
            }
            case 0x2004:
                break;
            case 0x2007: {
                int value = this.memory.read1(this.state.regV);

                //Buffered read
                if (this.state.regV < 0x3F00) {
                    int buffer = this.state.dataBuffer;
                    this.state.dataBuffer = value;
                    value = buffer;
                } else {
                    this.state.dataBuffer = this.memory.read1(this.state.regV - 0x1000);
                }

                if (this.state.flagAddressIncrement == 1) {
                    this.state.regV += 32;
                } else {
                    this.state.regV++;
                }

                return value;
            }
        }

        throw new IllegalStateException(String.format("Failed to read from register $%04x.", address));
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
                this.state.flagNmiOutput = bitPresent(value, 7);

                this.state.regT = (this.state.regT & 0x73FF) | ((value & 0x03) << 10);
                break;
            case 0x2001:
                this.state.flagGreyscale = bitPresent(value, 0);
                this.state.flagLeftmostBackground = bitPresent(value, 1);
                this.state.flagLeftmostSprites = bitPresent(value, 2);
                this.state.flagBackground = bitPresent(value, 3);
                this.state.flagSprites = bitPresent(value, 4);
                this.state.flagEmphRed = bitPresent(value, 5);
                this.state.flagEmphGreen = bitPresent(value, 6);
                this.state.flagEmphBlue = bitPresent(value, 7);
                break;
            case 0x2003:
                this.state.regOamAddr = value;
                break;
            case 0x2004:
                break;
            case 0x2005:
                //PPUSCROLL
                if (this.state.flagW == 0) {
                    this.state.regT = (this.state.regT & 0xFFE0) | (value >> 3);
                    this.state.regX = value & 0x07;
                    this.state.flagW = 1;
                } else {
                    this.state.regT &= 0x0C1F;
                    this.state.regT |= (value & 0x07) << 12;
                    this.state.regT |= (value & 0xF8) << 2;
                    this.state.flagW = 0;
                }
                break;
            case 0x2006:
                //PPUADDR
                if (this.state.flagW == 0) {
                    this.state.regT = (this.state.regT & 0x00FF) | ((value & 0x3F) << 8);
                    this.state.flagW = 1;
                } else {
                    this.state.regT = (this.state.regT & 0x7F00) | value;
                    this.state.regV = this.state.regT;
                    this.state.flagW = 0;
                }
                break;
            case 0x2007:
                //PPUDATA
                this.memory.write1(this.state.regV, value);
                if (this.state.flagAddressIncrement == 1) {
                    this.state.regV += 32;
                } else {
                    this.state.regV++;
                }
                break;
            case 0x4014:
                //OAMDMA
                int page = value << 8;
                for (int i = 0; i <= 0xFF; i++) {
                    int data = this.console.getCpu().getMemory().read1(page | i);
                    this.state.oamData[this.state.regOamAddr] = (byte) data;
                    this.state.regOamAddr++;
                }
                CpuState cpuState = this.console.getCpu().getState();
                cpuState.cycles += 513 + ((cpuState.cycles % 2 == 1) ? 1 : 0); //514 if odd cycles, 513 otherwise

                break;
            default:
                throw new IllegalStateException(String.format("Failed to write to register $%04x.", address));
        }
    }

    public void incX() {
        if ((this.state.regV & 0x001F) == 31) { //If coarse X == 31
            this.state.regV &= 0x7FE0; //Coarse X = 0
            this.state.regV ^= 0x0400; //Switch horizontal nametable
        } else {
            this.state.regV += 1; //Increment coarse X
        }
    }

    public void incY() {
        if ((this.state.regV & 0x7000) != 0x7000) { //If fine Y < 7
            this.state.regV += 0x1000; //Increment fine Y
        } else {
            this.state.regV &= 0x0FFF; //Fine Y = 0
            int y = (this.state.regV & 0x03E0) >> 5; //Let y = coarse Y
            if (y == 29) {
                y = 0; //Coarse Y = 0
                this.state.regV ^= 0x0800; //Switch vertical nametable
            } else if (y == 31) {
                y = 0; //Coarse Y = 0, nametable not switched
            } else {
                y++; //Increment coarse Y
            }

            this.state.regV = (this.state.regV & 0x8C1F) | (y << 5); //Put coarse Y back into v
        }
    }

    private void copyX() {
        //v: ....F.. ...EDCBA = t: ....F.. ...EDCBA
        this.state.regV = (this.state.regV & 0x7BE0) | (this.state.regT & 0x041F);
    }

    private void copyY() {
        //v: IHGF.ED CBA..... = t: IHGF.ED CBA.....
        this.state.regV = (this.state.regV & 0x041F) | (this.state.regT & 0x7BE0);
    }

    private int getLowTileAddress() {
        int fineY = this.state.regV >> 12;
        int tile = this.state.nametableByte;
        int table = this.state.flagBackgroundTable;
        return 0x1000 * table + 16 * tile + fineY;
    }

    private void calculateTileData() {
        int data = 0;
        for (int i = 0; i < 8; i++) {
            data  <<= 4;

            data |= this.state.attribTableByte
                    | ((this.state.lowTileByte & 0x80) >> 7)
                    | ((this.state.highTileByte & 0x80) >> 6);

            this.state.lowTileByte <<= 1;
            this.state.highTileByte <<= 1;
        }

        this.state.tileData |= Integer.toUnsignedLong(data);
    }

    private void processRenderLine() {
        boolean fetchCycle = (this.state.dot >= 1 && this.state.dot <= 256) || //Visible cycles
                (this.state.dot >= 321 && this.state.dot <= 336);   //Pre-fetch cycles

        if (fetchCycle) {
            this.state.tileData <<= 4;
            //TODO: Split up fetch into setting addr bus value and reading
            switch ((this.state.dot - 1) % 8) {
                case 0: { //NT Byte
                    int address = 0x2000 | (this.state.regV & 0x0FFF);
                    this.state.nametableByte = this.memory.read1(address);
                    break;
                }
                case 2: { //AT byte
                    int v = this.state.regV;
                    //Gets address of 32x32 region currently at
                    int address = 0x23C0 | (v & 0x0C00) | ((v >> 4) & 0x38) | ((v >> 2) & 0x07);
                    //Maybe? Gets the shift to get the two bit palette id for the block (16x16)
                    int shift = ((v >> 4) & 4) | (v & 2);
                    this.state.attribTableByte = ((this.memory.read1(address) >> shift) & 3) << 2;
                    break;
                }
                case 4: {
                    this.state.lowTileByte = this.memory.read1(getLowTileAddress());
                    break;
                }
                case 6:
                    this.state.highTileByte = this.memory.read1(getLowTileAddress() + 8);
                    break;
                case 7:
                    calculateTileData();

                    incX();
                    break;
            }

            if (this.state.dot == 256) {
                incY();
            }
        } else if (this.state.dot == 257) {
            copyX();
        }
    }

    private void processPrerenderLine() {
        if (this.state.dot == 1) {
            this.state.flagNmiOccurred = false;
            this.state.flagSpriteZeroHit = false;
            this.state.flagSpriteOverflow = false;
        }

        if (this.state.flagBackground || this.state.flagSprites) { //Rendering enabled
            processRenderLine();

            if (this.state.dot >= 280 && this.state.dot <= 304) {
                copyY();
            }
        }
    }

    //https://wiki.nesdev.com/w/images/d/d1/Ntsc_timing.png
    public boolean tick() {
        boolean rerender = false;

        this.state.cycle++;
        this.state.dot++;

        if (this.state.dot == 341) {
            this.state.dot = 0;
            this.state.scanline++;

            if (this.state.scanline == 240) {
                this.state.frame++;
                rerender = true;
            } else if (this.state.scanline == 262) {
                this.state.scanline = 0;

                //Skip first dot of first scanline on odd frames
                if ((this.state.flagBackground || this.state.flagSprites) && this.state.oddFrame) {
                    this.state.dot++;
                }
                this.state.oddFrame = !this.state.oddFrame;
            }
        }

        if (this.state.scanline == 261) {
            processPrerenderLine();
        } else if (this.state.scanline < 240 && (this.state.flagBackground || this.state.flagSprites)) {
            //Visible scanline
            if (this.state.dot >= 1 && this.state.dot <= 256) {
                renderPixel();
                if (NesEmu.DEBUG) {
                    rerender = true;
                }
            }
            processRenderLine();
        } else if (this.state.scanline == 241 && this.state.dot == 1) {
            this.state.flagNmiOccurred = true;
            if (this.state.flagNmiOutput) {
                this.console.getCpu().raiseNmi();
            }
        }

        return rerender;
    }

    public PpuState getState() {
        return this.state;
    }

    public PpuMemory getMemory() {
        return this.memory;
    }

}
