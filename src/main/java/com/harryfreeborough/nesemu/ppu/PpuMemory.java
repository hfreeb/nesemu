package com.harryfreeborough.nesemu.ppu;

import com.harryfreeborough.nesemu.Console;
import com.harryfreeborough.nesemu.rom.MirroringMode;
import com.harryfreeborough.nesemu.utils.MemorySpace;
import com.harryfreeborough.nesemu.utils.Preconditions;

/**
 * Manages all reads and writes to the CPU memory space.
 *
 * $0000-$0FFF |> Pattern table 0
 * $1000-$1FFF |> Pattern table 1
 * $2000-$23FF |> Nametable 0
 * $2400-$27FF |> Nametable 1
 * $2800-$2BFF |> Nametable 2
 * $2C00-$2FFF |> Nametable 3
 * $3000-$3EFF |> Mirrors of $2000-$2EFF
 * $3F00-$3F1F |> Palette RAM indices
 * $3F20-$3FFF |> Mirrors of $3F00-$3F1F
 */
public class PpuMemory implements MemorySpace {

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
        if (address < 0x2000) { //Read from the active mapper
            return this.console.getMapper().read1(address);
        } else if (address < 0x3F00) { //Nametable data
            MirroringMode mode = console.getMapper().getMirroringMode();
            return Byte.toUnsignedInt(state.nametableData[mirrorAddress(mode, address % 2048)]);
        } else { //Palette data
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
        if (address < 0x2000) { //Write to mapper
            this.console.getMapper().write1(address, value);
        } else if (address < 0x3000) { //Nametable data
            MirroringMode mode = this.console.getMapper().getMirroringMode();
            state.nametableData[mirrorAddress(mode, address)] = (byte) value;
        } else { //Palette data
            if (address >= 0x3F10 && address % 4 == 0) {
                address -= 16;
            }

            state.palleteData[address % 0x20] = (byte) value;
        }
    }

    /**
     * Returns the physical address to the specified address
     * after adjusting for the method used for mirroring the
     * memory.
     */
    private int mirrorAddress(MirroringMode mode, int address) {
        switch (mode) {
            case HORIZONTAL:
                int table = Math.floorDiv(address % 0x1000, 0x0400);
                int offset = address % 0x0400;
                return Math.floorDiv(table, 2) * 0x0400 + offset;
            case VERTICAL:
                return address % 0x0800;
            case FOUR_SCREEN:
                return address;
            case SINGLE_LOW:
                return address % 0x0400;
            case SINGLE_HIGH:
                return 0x400 + (address % 0x400);
        }

        throw new IllegalStateException(
                String.format("%s not implemented for translating namespace address", mode.name())
        );
    }

}
