package com.harryfreeborough.nesemu.ppu;

import com.harryfreeborough.nesemu.rom.MirroringMode;
import com.harryfreeborough.nesemu.rom.RomData;
import com.harryfreeborough.nesemu.utils.Memory;
import com.harryfreeborough.nesemu.utils.Preconditions;

public class PpuMemory implements Memory {
    
    private final PpuState state;
    private final RomData romData;
    
    public PpuMemory(PpuState state, RomData romData) {
        this.state = state;
        this.romData = romData;
    }
    
    @Override
    public int read1(int address) {
        Preconditions.checkArgument(address < 0x4000, "Address $%04X out of range.", address);
        
        if (address < 0x2000) {
            return Byte.toUnsignedInt(this.romData.getChrRomData()[address]);
        } else if (address < 0x3F00) {
            MirroringMode mode = this.romData.getMirroringMode();
            return this.state.nametableData[mirrorAddress(mode, address % 2048)];
        } else {
            return this.state.palleteData[address % 0x20];
        }
    }
    
    @Override
    public void write1(int address, int value) {
        Preconditions.checkArgument(address < 0x4000, "Address $%04X out of range.", address);
        Preconditions.checkArgument(value <= 0xFF, "Value $%X too large.", value);
        
        if (address >= 0x2000) {
            if (address < 0x3000) {
                MirroringMode mode = this.romData.getMirroringMode();
                this.state.nametableData[mirrorAddress(mode, address % 2048)] = (byte) value;
            } else {
                this.state.palleteData[address % 0x20] = (byte) value;
            }
        } else {
            throw new IllegalStateException(String.format("Failed to write to address $%02X", address));
        }
        
    }
    
    private int mirrorAddress(MirroringMode mode, int address) {
        switch (mode) {
            case HORIZONTAL:
                int table = (address % 0x2000) / 0x0400;
                int offset = address % 0x0400;
                return 0x2000 + Math.floorDiv(table, 2) * 0x0400 + offset;
            case VERTICAL:
                return 0x2000 + address % 0x0800;
            case FOUR_SCREEN:
                return address;
        }
        
        throw new IllegalStateException(String.format("%s not implemented for translating namespace address"));
    }
    
}
