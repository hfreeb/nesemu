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

/**
 * Holds together all the components of the emulator.
 */
public class Console {

    private final Cpu cpu;
    private final Ppu ppu;
    private Cartridge cartridge;
    private Mapper mapper;

    private boolean saveQueued;
    private boolean loadQueued;
    private boolean resetQueued;
    private CpuState cpuStateSave;
    private PpuState ppuStateSave;

    public Console(Cartridge cartridge) {
        setCartridge(cartridge);

        CpuMemory cpuMemory = new CpuMemory(this);
        this.cpu = new Cpu(cpuMemory);
        this.cpu.getState().initPc(cpuMemory);

        PpuMemory ppuMemory = new PpuMemory(this);
        this.ppu = new Ppu(this, ppuMemory);
    }

    public void reset() {
        this.cpu.getState().reset();
        this.cpu.getState().initPc(this.cpu.getMemory());
        this.ppu.getState().reset();
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
        if (this.resetQueued) {
            reset();
            this.resetQueued = false;
            this.saveQueued = false;
            this.loadQueued = false;
        } else if (this.saveQueued) {
            this.cpuStateSave = this.cpu.getState().clone();
            this.ppuStateSave = this.ppu.getState().clone();
            this.saveQueued = false;
        } else if (this.loadQueued && this.cpuStateSave != null) {
            this.cpu.getState().copy(this.cpuStateSave);
            this.ppu.getState().copy(this.ppuStateSave);
            this.loadQueued = false;
        }
    }

    public void queueSave() {
        this.saveQueued = true;
    }

    public void queueLoad() {
        this.loadQueued = true;
    }

    public void queueReset() {
        this.resetQueued = true;
    }

}
