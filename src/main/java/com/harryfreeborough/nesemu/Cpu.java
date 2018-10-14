package com.harryfreeborough.nesemu;

import com.harryfreeborough.nesemu.processor.InstructionProcessor;
import com.harryfreeborough.nesemu.processor.MappedInstructionProcessor;

public class Cpu {

    private final MemoryBus bus;
    private final CpuState state;
    private final InstructionProcessor processor;

    public Cpu(MemoryBus bus, CpuState state) {
        this.bus = bus;
        this.state = state;

        this.processor = new MappedInstructionProcessor(this.state, this.bus);
    }
    
    public void tick1() {
    
    }

    public void execute() {
        this.processor.process();
    }

}
