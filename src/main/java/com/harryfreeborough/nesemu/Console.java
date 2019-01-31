package com.harryfreeborough.nesemu;

import com.harryfreeborough.nesemu.cpu.Cpu;
import com.harryfreeborough.nesemu.cpu.CpuMemory;
import com.harryfreeborough.nesemu.cpu.CpuState;
import com.harryfreeborough.nesemu.mapper.Mapper;
import com.harryfreeborough.nesemu.mapper.Mapper0;
import com.harryfreeborough.nesemu.mapper.Mapper1;
import com.harryfreeborough.nesemu.ppu.Ppu;
import com.harryfreeborough.nesemu.ppu.PpuMemory;
import com.harryfreeborough.nesemu.ppu.PpuState;
import com.harryfreeborough.nesemu.rom.Cartridge;

public class Console {

    private final Cpu cpu;
    private final Ppu ppu;
    private Cartridge cartridge;
    private Mapper mapper;

    private boolean saveQueued;
    private CpuState cpuStateSave;
    private PpuState ppuStateSave;

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

    public void frameEnd() {
        if (this.saveQueued) {
            this.cpuStateSave = this.cpu.getState().clone();
            this.ppuStateSave = this.ppu.getState().clone();
            this.saveQueued = false;
        }
    }

    public void queueSave() {
        this.saveQueued = true;
    }

    public void loadSave() {
        if (this.cpuStateSave != null) {
            this.cpu.getState().copy(this.cpuStateSave);
            this.ppu.getState().copy(this.ppuStateSave);
        }
    }

}
