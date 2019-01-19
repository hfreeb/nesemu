package com.harryfreeborough.nesemu;

import com.harryfreeborough.nesemu.cpu.Cpu;
import com.harryfreeborough.nesemu.cpu.CpuMemory;
import com.harryfreeborough.nesemu.cpu.CpuState;
import com.harryfreeborough.nesemu.ppu.Ppu;
import com.harryfreeborough.nesemu.ppu.PpuMemory;
import com.harryfreeborough.nesemu.ppu.PpuState;
import com.harryfreeborough.nesemu.rom.RomData;
import com.harryfreeborough.nesemu.rom.RomReader;
import com.harryfreeborough.nesemu.utils.Preconditions;

import java.nio.file.Paths;

public class NesEmu {

    public static final boolean DEBUG = System.getProperty("debug") != null;
    private static final long SCANLINE_CPU_CYCLES = 114; //263 / 3 TODO: should be 113 + 2/3

    public static void main(String[] args) {
        new NesEmu().run(args[0]);
    }
    
    public void run(String romPath) {
        RomData data = RomReader.read(Paths.get(romPath))
                .orElseThrow(() -> new RuntimeException("Failed to read rom."));
    
        Preconditions.checkState(data.getMapperId() == 0, "Only the NROM mapper is currently supported.");
    
        PpuState ppuState = new PpuState();
        PpuMemory ppuMemory = new PpuMemory(ppuState, data);
        Ppu ppu = new Ppu(ppuState, ppuMemory);
        CpuState cpuState = new CpuState();
        
        CpuMemory bus = new CpuMemory(ppu, cpuState, data);

        DebugGen debug = new DebugGen(cpuState, ppuState, bus);
        Cpu cpu = new Cpu(bus, cpuState, debug);

        while (cpu.tick()) {
            if (cpuState.cycles % SCANLINE_CPU_CYCLES == 0) {
                ppu.runScanline();
            }

        }
    }
    
}
