package com.harryfreeborough.nesemu.cpu;

import com.harryfreeborough.nesemu.Console;
import com.harryfreeborough.nesemu.utils.Memory;
import com.harryfreeborough.nesemu.utils.MemoryUtils;
import com.harryfreeborough.nesemu.utils.Preconditions;

import java.util.LinkedList;
import java.util.Queue;

public class CpuMemory implements Memory {

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
        } else if (address == 0x4017) {
            return 0;
        } else if (address < 0x4020) {
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

    public void setButtonState(int button, boolean value) {
        this.buttonState[button] = value;
    }

}
