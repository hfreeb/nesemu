package com.harryfreeborough.nesemu.cpu;

import com.harryfreeborough.nesemu.Console;
import com.harryfreeborough.nesemu.utils.MemorySpace;
import com.harryfreeborough.nesemu.utils.MemoryUtils;
import com.harryfreeborough.nesemu.utils.Preconditions;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Manages all reads and writes to the CPU memory space.
 */
public class CpuMemory implements MemorySpace {

    public final boolean[] buttonState = new boolean[8];
    public final Queue<Boolean> buttonStateCache = new LinkedList<>();
    private final Console console;

    public CpuMemory(Console console) {
        this.console = console;
    }

    @Override
    public int read1(int address) {
        Preconditions.checkArgument(address <= 0xFFFF, "Address out of range.");

        CpuState state = this.console.getCpu().getState();
        if (address < 0x2000) {
            return Byte.toUnsignedInt(state.internalRam[address % 0x800]);
        } else if (address < 0x4000) {
            return this.console.getPpu().readRegister(0x2000 + (address % 8));
        } else if (address == 0x4016) {
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
        } else if (address == 0x4017 || address == 0x4015 || address == 0x58A9 /*???*/) {
            return 0;
        } else if (address < 0x6000) {
            //APU and I/O registers
        } else {
            return this.console.getMapper().read1(address);
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
                this.buttonStateCache.clear();
                for (boolean i : this.buttonState) {
                    this.buttonStateCache.offer(i);
                }
            }
        } else if (address < 0x4020) {
            //I/O and audio registers
        } else {
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
