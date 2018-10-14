package com.harryfreeborough.nesemu.addressing;

import com.harryfreeborough.nesemu.MemoryBus;
import com.harryfreeborough.nesemu.CpuState;
import com.harryfreeborough.nesemu.utils.MemoryUtils;

/**
 * Immediate mode - read the data straight from the program.
 */
public class ImmediateMode implements InstructionMode {

    public int read1(MemoryBus bus, CpuState store) {
        return MemoryUtils.programPop1(bus, store);
    }

    @Override
    public void write1(MemoryBus bus, CpuState store, int value) {
        throw new UnsupportedOperationException("You can not write with immediate mode");
    }

}
