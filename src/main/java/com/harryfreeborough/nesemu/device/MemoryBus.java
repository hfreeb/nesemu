package com.harryfreeborough.nesemu.device;

import com.harryfreeborough.nesemu.exceptions.BusException;
import com.harryfreeborough.nesemu.ppu.Ppu;
import com.harryfreeborough.nesemu.utils.Preconditions;

import java.util.NavigableSet;
import java.util.Optional;
import java.util.TreeSet;

public class MemoryBus {

    private final Ppu ppu;
    private final byte[] internalRam = new byte[0x800];

    public MemoryBus(Ppu ppu) {
        this.ppu = ppu;
    }

    public int read1(int address) {
        Preconditions.checkArgument(address <= 0xFFFF, "Address out of range.");
        address &= 0xFFFF;

        if (address < 0x2000) {
            return this.internalRam[address % 0x800];
        } else if (address < 0x4000) {
            return this.ppu.readRegister(0x2000 + (address % 8));
        } else if (address < 0x4020) {
            //APU and I/O registers
        } else {
            //Cartridge space
        }

        throw new IllegalStateException(String.format("Failed to read from address %02X", address));
    }
    public int read2(int address) {
        return read1(address) | (read1(address + 1) << 8);
    }
    
    public void write1(int address, int value) {
        Preconditions.checkArgument(address <= 0xFFFF, "Address out of range.");
        Preconditions.checkArgument(value <= 0xFF, "Value too large.");

        if (address < 0x2000) {
            this.internalRam[address % 0x800] = (byte) value;
        } else if (address < 0x4000) {
            this.ppu.writeRegister(0x2000 + (address % 8), value);
        } else if (address < 0x4020) {
            //APU and I/O registers
        } else {
            //Cartridge space
        }

        throw new IllegalStateException(String.format("Failed to write to address %02X", address));
    }
    
    public void write2(int address, int value) {
        write1(address, value & 0xFF);
        write1(address, value >> 8);
    }
    
}
