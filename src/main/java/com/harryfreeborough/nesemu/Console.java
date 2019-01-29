package com.harryfreeborough.nesemu;

import com.harryfreeborough.nesemu.cpu.Cpu;
import com.harryfreeborough.nesemu.cpu.CpuMemory;
import com.harryfreeborough.nesemu.mapper.Mapper1;
import com.harryfreeborough.nesemu.ppu.Ppu;
import com.harryfreeborough.nesemu.ppu.PpuMemory;
import com.harryfreeborough.nesemu.rom.Cartridge;
import com.harryfreeborough.nesemu.mapper.Mapper;
import com.harryfreeborough.nesemu.mapper.Mapper0;

public class Console {
    
    private final Cpu cpu;
    private final Ppu ppu;
    private Cartridge cartridge;
    private Mapper mapper;

    public Console(Cartridge cartridge) {
        setCartridge(cartridge);

        CpuMemory cpuMemory = new CpuMemory(this);
        this.cpu = new Cpu(cpuMemory);
        
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

    public void setCartridge(Cartridge cartridge) {
        this.cartridge = cartridge;

        switch (this.cartridge.getMapperId()) {
            case 0:
                this.mapper = new Mapper0(cartridge);
                break;
            case 1:
                this.mapper = new Mapper1(cartridge);
                break;
            default:
                throw new IllegalStateException(
                        String.format("Mapper %d not supported.", this.cartridge.getMapperId())
                );
        }
    }

    public Mapper getMapper() {
        return this.mapper;
    }

}
