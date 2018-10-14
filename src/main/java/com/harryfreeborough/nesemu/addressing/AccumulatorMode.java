package com.harryfreeborough.nesemu.addressing;

import com.harryfreeborough.nesemu.MemoryBus;
import com.harryfreeborough.nesemu.CpuState;

public class AccumulatorMode implements InstructionMode {

    @Override
    public int read1(MemoryBus bus, CpuState store) {
        return store.regA;
    }

    @Override
    public void write1(MemoryBus bus, CpuState store, int value) {
        store.regA = value;
    }

}
