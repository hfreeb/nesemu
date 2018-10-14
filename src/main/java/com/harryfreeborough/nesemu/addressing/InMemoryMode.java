package com.harryfreeborough.nesemu.addressing;

import com.harryfreeborough.nesemu.MemoryBus;
import com.harryfreeborough.nesemu.CpuState;

public class InMemoryMode implements InstructionMode {

    private final int address;

    public InMemoryMode(int address) {
        this.address = address;
    }

    @Override
    public int read1(MemoryBus bus, CpuState store) {
        return bus.read1(this.address);
    }

    @Override
    public void write1(MemoryBus bus, CpuState store, int value) {
        bus.write1(this.address, value);
    }

}
