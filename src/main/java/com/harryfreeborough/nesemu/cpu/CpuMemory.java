package com.harryfreeborough.nesemu.cpu;

import com.harryfreeborough.nesemu.Console;
import com.harryfreeborough.nesemu.rom.Cartridge;
import com.harryfreeborough.nesemu.utils.Memory;
import com.harryfreeborough.nesemu.utils.MemoryUtils;
import com.harryfreeborough.nesemu.utils.Preconditions;

import java.util.Arrays;

public class CpuMemory implements Memory {
    
    private final Console console;
    
    public CpuMemory(Console console) {
        this.console = console;
    }
    
    @Override
    public int read1(int address) {
        Preconditions.checkArgument(address <= 0xFFFF, "Address out of range.");
    
        CpuState state = this.console.getCpu().getState();
        Cartridge cartridge = this.console.getCartridge();
        if (address < 0x2000) {
            return Byte.toUnsignedInt(state.internalRam[address % 0x800]);
        } else if (address < 0x4000) {
            return this.console.getPpu().readRegister(0x2000 + (address % 8));
        } else if (address == 0x4016) {
            if (state.flagStrobe) {
                return 0x40 | (state.buttonState[0] ? 1 : 0);
            } else {
                Boolean val = state.buttonStateCache.remove();
                int i = 1;
                if (val != null) {
                    i = val ? 1 : 0;
                }

                return 0x40 | i;
            }
        } else if (address == 0x4017) {
            return 0;
        } else if (address < 0x4020) {
            //APU and I/O registers
        } else if (address < 0x8000) {
            //Ignore
        } else if (address < 0xC000) {
            //First 16KiB of ROM
            return Byte.toUnsignedInt(cartridge.getPrgRomData()[address % 0x4000]);
        } else {
            //Last 16KiB of ROM
            return Byte.toUnsignedInt(
                    cartridge.getPrgRomData()[(cartridge.getPrgRomSize() - 1) * 0x4000 + address % 0x4000]
            );
        }
        
        throw new IllegalStateException(String.format("Failed to read from address $%02X", address));
    }
    
    @Override
    public void write1(int address, int value) {
        Preconditions.checkArgument(address <= 0xFFFF, "Address out of range.");
        Preconditions.checkArgument(value <= 0xFF, "Value too large.");
    
        CpuState state = this.console.getCpu().getState();
        if (address < 0x2000) {
            state.internalRam[address % 0x800] = (byte) value;
        } else if (address < 0x4000) {
            this.console.getPpu().writeRegister(0x2000 + (address % 8), value);
        } else if (address == 0x4014) {
            this.console.getPpu().writeRegister(address, value);
        } else if (address == 0x4016) {
            if (MemoryUtils.bitPresent(value, 0)) {
                state.flagStrobe = true;
            } else {
                state.flagStrobe = false;
                state.buttonStateCache.clear();
                for (boolean i : state.buttonState) {
                    state.buttonStateCache.offer(i);
                }
            }
        } else if (address < 0x6000) {
            //I/O and audio registers
        } else {
            throw new IllegalStateException(String.format("Failed to write to address $%02X", address));
        }
        
    }
    
}
