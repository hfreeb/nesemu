package com.harryfreeborough.nesemu;

import com.harryfreeborough.nesemu.cpu.Cpu;
import com.harryfreeborough.nesemu.cpu.CpuMemory;
import com.harryfreeborough.nesemu.ppu.Ppu;
import com.harryfreeborough.nesemu.ppu.PpuMemory;
import com.harryfreeborough.nesemu.rom.Cartridge;

public class Console {
    
    private final Cpu cpu;
    private final Ppu ppu;
    private final Cartridge cartridge;
    
    public Console(Cartridge cartridge) {
        this.cartridge = cartridge;
        
        CpuMemory cpuMemory = new CpuMemory(this);
        this.cpu = new Cpu(cpuMemory, new DebugGen(this));
        
        PpuMemory ppuMemory = new PpuMemory(this);
        this.ppu = new Ppu(this, ppuMemory);
        
        reset();
    }
    
    public void reset() {
        this.cpu.reset();
    }
    
    public Cpu getCpu() {
        return this.cpu;
    }
    
    public Ppu getPpu() {
        return this.ppu;
    }
    
    public Cartridge getCartridge() {
        return this.cartridge;
    }
    
}
