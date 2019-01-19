package com.harryfreeborough.nesemu.cpu;

import com.harryfreeborough.nesemu.ppu.Ppu;
import com.harryfreeborough.nesemu.rom.RomData;
import com.harryfreeborough.nesemu.utils.Memory;
import com.harryfreeborough.nesemu.utils.Preconditions;

public class CpuMemory implements Memory {
    
    private final Ppu ppu;
    private final CpuState state;
    private final RomData romData;
    
    public CpuMemory(Ppu ppu, CpuState state, RomData romData) {
        this.ppu = ppu;
        this.state = state;
        this.romData = romData;
    }
    
    @Override
    public int read1(int address) {
        Preconditions.checkArgument(address <= 0xFFFF, "Address out of range.");
        
        if (address < 0x2000) {
            return Byte.toUnsignedInt(this.state.internalRam[address % 0x800]);
        } else if (address < 0x4000) {
            return this.ppu.readRegister(0x2000 + (address % 8));
        } else if (address < 0x4020) {
            //APU and I/O registers
        } else if (address < 0x8000) {
            //Ignore
        } else if (address < 0xC000) {
            //First 16KiB of ROM
            return Byte.toUnsignedInt(this.romData.getPrgRomData()[address % 0x4000]);
        } else {
            //Last 16KiB of ROM
            return Byte.toUnsignedInt(
                    this.romData.getPrgRomData()[(this.romData.getPrgRomSize() - 1) * 0x4000 + address % 0x4000]
            );
        }
        
        throw new IllegalStateException(String.format("Failed to read from address $%02X", address));
    }
    
    @Override
    public void write1(int address, int value) {
        Preconditions.checkArgument(address <= 0xFFFF, "Address out of range.");
        Preconditions.checkArgument(value <= 0xFF, "Value too large.");
        
        if (address < 0x2000) {
            this.state.internalRam[address % 0x800] = (byte) value;
        } else if (address < 0x4000) {
            this.ppu.writeRegister(0x2000 + (address % 8), value);
        } else {
            throw new IllegalStateException(String.format("Failed to write to address $%02X", address));
        }
        
    }
    
}
