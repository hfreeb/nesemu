package com.harryfreeborough.nesemu;

import com.harryfreeborough.nesemu.device.Memory;
import com.harryfreeborough.nesemu.device.MemoryBus;
import com.harryfreeborough.nesemu.device.MemoryMirror;

public class Console {
    
    private final Cpu cpu;
    
    public Console() {
        MemoryBus bus = new MemoryBus();
        CpuState state = new CpuState();
        this.cpu = new Cpu(bus, state);
        
        bus.registerDevice(new Memory(0x0800, false), 0x0000);
        bus.registerDevice(new MemoryMirror(bus, 0x1800, 0x0000, 0x0800), 0x0800);
    }
    
    public Cpu getCpu() {
        return this.cpu;
    }
    
}
