package com.harryfreeborough.nesemu.addressing;

import com.harryfreeborough.nesemu.MemoryBus;
import com.harryfreeborough.nesemu.CpuState;

public interface InstructionMode {

    InstructionMode IGNORED = new IgnoredMode();
    InstructionMode IMMEDIATE = new ImmediateMode();
    InstructionMode ACCUMULATOR = new AccumulatorMode();

    static InstructionMode createInMemoryMode(int address) {
        return new InMemoryMode(address);
    }

    int read1(MemoryBus bus, CpuState store);

    void write1(MemoryBus bus, CpuState store, int value);

}
