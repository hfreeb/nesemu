package com.harryfreeborough.nesemu.ppu;

import com.harryfreeborough.nesemu.Console;
import com.harryfreeborough.nesemu.rom.Cartridge;
import com.harryfreeborough.nesemu.rom.MirroringMode;
import com.harryfreeborough.nesemu.utils.Memory;
import com.harryfreeborough.nesemu.utils.Preconditions;

public class PpuMemory implements Memory {
    
    private final Console console;
    
    public PpuMemory(Console console) {
        this.console = console;
    }
    
    @Override
    public int read1(int address) {
        Preconditions.checkArgument(address < 0x4000, "Address $%04X out of range.", address);
    
        Cartridge cartridge = this.console.getCartridge();
        PpuState state = this.console.getPpu().getState();
        if (address < 0x2000) {
            return Byte.toUnsignedInt(cartridge.getChrRomData()[address]);
        } else if (address < 0x3F00) {
            MirroringMode mode = cartridge.getMirroringMode();
            return state.nametableData[mirrorAddress(mode, address % 2048)];
        } else {
            return state.palleteData[address % 0x20];
        }
    }
    
    @Override
    public void write1(int address, int value) {
        Preconditions.checkArgument(address < 0x4000, "Address $%04X out of range.", address);
        Preconditions.checkArgument(value <= 0xFF, "Value $%X too large.", value);
    
        Cartridge cartridge = this.console.getCartridge();
        PpuState state = this.console.getPpu().getState();
        if (address >= 0x2000) {
            if (address < 0x3000) {
                MirroringMode mode = cartridge.getMirroringMode();
                state.nametableData[mirrorAddress(mode, address)] = (byte) value;
            } else {
                state.palleteData[address % 0x20] = (byte) value;
            }
        } else {
            throw new IllegalStateException(String.format("Failed to write to address $%02X", address));
        }
        
    }
    
    private int mirrorAddress(MirroringMode mode, int address) {
        switch (mode) {
            case HORIZONTAL:
                int table = (address % 0x1000) / 0x0400;
                int offset = address % 0x0400;
                return Math.floorDiv(table, 2) * 0x0400 + offset;
            case VERTICAL:
                return address % 0x0800;
            case FOUR_SCREEN:
                return address;
        }
        
        throw new IllegalStateException(
                String.format("%s not implemented for translating namespace address", mode.name())
        );
    }
    
}
