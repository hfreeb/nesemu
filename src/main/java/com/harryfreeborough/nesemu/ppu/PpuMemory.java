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
        Preconditions.checkArgument(address < 0x8000, "Address $%04X out of range.", address);
        //Ignore 15th bit but anymore than that error on
        address &= 0x3FFF;

        PpuState state = this.console.getPpu().getState();
        if (address < 0x2000) {
            return this.console.getMapper().read1(address);
        } else if (address < 0x3F00) {
            MirroringMode mode = console.getMapper().getMirroringMode();
            return Byte.toUnsignedInt(state.nametableData[mirrorAddress(mode, address % 2048)]);
        } else {
            if (address >= 0x3F10 && address % 4 == 0) {
                address -= 16;
            }
            return state.palleteData[address % 0x20];
        }
    }

    @Override
    public void write1(int address, int value) {
        Preconditions.checkArgument(address < 0x8000, "Address $%04X out of range.", address);
        Preconditions.checkArgument(value <= 0xFF, "Value $%X too large.", value);
        //Ignore 15th bit but anymore than that error on
        address &= 0x3FFF;

        PpuState state = this.console.getPpu().getState();
        if (address < 0x2000) {
            this.console.getCartridge().getChrRomData()[address] = (byte) value;
        } else if (address < 0x3000) {
            MirroringMode mode = this.console.getMapper().getMirroringMode();
            state.nametableData[mirrorAddress(mode, address)] = (byte) value;
        } else if (address < 0x4000) { //TODO: Should just be else
            if (address >= 0x3F10 && address % 4 == 0) {
                address -= 16;
            }
            
            state.palleteData[address % 0x20] = (byte) value;
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
