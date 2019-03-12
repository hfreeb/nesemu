package com.harryfreeborough.nesemu.cpu;

import com.harryfreeborough.nesemu.Console;
import com.harryfreeborough.nesemu.mapper.Mapper;
import com.harryfreeborough.nesemu.utils.MemorySpace;
import com.harryfreeborough.nesemu.utils.MemoryUtils;
import com.harryfreeborough.nesemu.utils.Preconditions;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Manages all reads and writes to the CPU memory space.
 *
 * $0000-$07FF |> 2KiB internal memory
 * $0800-$1FFF |> Mirrors of $0000-$07FF
 * $2000-$2007 |> PPU registers
 * $2008-$3FFF |> Mirrors of $2000-$2007
 * $4000-$4017 |> APU and I/O registers
 * $4020-$FFFF |> Cartridge space, controlled by the active {@link Mapper}.
 */
public class CpuMemory implements MemorySpace {

    private final boolean[] buttonState = new boolean[8];
    private final Queue<Boolean> buttonStateCache = new LinkedList<>();
    private final Console console;

    public CpuMemory(Console console) {
        this.console = console;
    }

    @Override
    public int read1(int address) {
        Preconditions.checkArgument(address <= 0xFFFF, "Address out of range.");

        CpuState state = this.console.getCpu().getState();
        if (address < 0x2000) { //Internal ram
            return Byte.toUnsignedInt(state.internalRam[address % 0x800]);
        } else if (address < 0x4000) { //PPU registers
            return this.console.getPpu().readRegister(0x2000 + (address % 8));
        } else if (address == 0x4016) { //Controller 1 register
            if (state.flagStrobe) {
                return 0x40 | (this.buttonState[0] ? 1 : 0);
            } else {
                Boolean val = this.buttonStateCache.remove();
                int i = 1;
                if (val != null) {
                    i = val ? 1 : 0;
                }

                return 0x40 | i;
            }
        } else if (address == 0x4017 || address == 0x4015) {
            //Secondary controller and APU register
            //Returning 0 keeps the game running without those implemented
            return 0;
        } else if (address < 0x6000) { //APU and I/O registers
            //Ignore
        } else { //Read from the active mapper
            return this.console.getMapper().read1(address);
        }

        throw new IllegalStateException(String.format("Failed to read from address $%02X", address));
    }

    @Override
    public void write1(int address, int value) {
        Preconditions.checkArgument(address <= 0xFFFF, "Address out of range.");
        Preconditions.checkArgument(value <= 0xFF, "Value too large.");

        CpuState state = this.console.getCpu().getState();
        if (address < 0x2000) { //Internal ram
            state.internalRam[address % 0x800] = (byte) value;
        } else if (address < 0x4000) { //PPU registers
            this.console.getPpu().writeRegister(0x2000 + (address % 8), value);
        } else if (address == 0x4014) { //OAMDMA PPU register
            this.console.getPpu().writeRegister(address, value);
        } else if (address == 0x4016) { //Controller 1 register
            if (MemoryUtils.bitSet(value, 0)) {
                //Set strobing on, so button state is continuously recorded
                //If the button state register is read during this period,
                //only the state of the first button (Z) will be returned.
                state.flagStrobe = true;
            } else {
                //Current button state is cleared and saved in a queue, and future reads
                //of the controller register will each time remove and return a bit from this queue.
                state.flagStrobe = false;
                this.buttonStateCache.clear();
                for (boolean i : this.buttonState) {
                    this.buttonStateCache.offer(i);
                }
            }
        } else if (address < 0x4020) { //I/O and audio registers
            //Ignore
        } else { //Write to the active Mapper
            this.console.getMapper().write1(address, value);
        }
    }

    public void setButtonDown(int button, boolean down) {
        this.buttonState[button] = down;
    }

    public void stackPush1(int value, CpuState state) {
        write1(0x100 | state.regSp, value);
        state.regSp = (state.regSp - 1) & 0xFF;
    }

    public void stackPush2(int value, CpuState state) {
        stackPush1(value >> 8, state);
        stackPush1(value & 0xFF, state);
    }

    public int stackPop1(CpuState state) {
        state.regSp = (state.regSp + 1) & 0xFF;
        return read1(0x100 | state.regSp);
    }

    public int stackPop2(CpuState state) {
        int lsb = stackPop1(state);
        int msb = stackPop1(state);
        return lsb | (msb << 8);
    }

}
