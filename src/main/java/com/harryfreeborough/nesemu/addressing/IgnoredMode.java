package com.harryfreeborough.nesemu.addressing;

import com.harryfreeborough.nesemu.MemoryBus;
import com.harryfreeborough.nesemu.CpuState;

public class IgnoredMode implements InstructionMode {

    @Override
    public int read1(MemoryBus bus, CpuState store) {
        throw new UnsupportedOperationException("You can not read from IgnoredMode.");
    }

    @Override
    public void write1(MemoryBus bus, CpuState store, int value) {
        throw new UnsupportedOperationException("You can not write to IgnoredMode.");
    }
}
