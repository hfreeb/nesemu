package com.harryfreeborough.nesemu;

import com.harryfreeborough.nesemu.device.MemoryBus;
import com.harryfreeborough.nesemu.ppu.Ppu;
import com.harryfreeborough.nesemu.rom.RomData;
import com.harryfreeborough.nesemu.rom.RomReader;

import java.nio.file.Paths;

public class NesEmu {

    private static final long SCANLINE_CPU_CYCLES = 114; //263 / 3 (should be 113 + 2/3, adjusted by thirdCycles)

    public static void main(String[] args) {
        new NesEmu().run(args[0]);
    }
    
    public void run(String romPath) {
        RomData data = RomReader.read(Paths.get(romPath))
                .orElseThrow(() -> new RuntimeException("Failed to read rom."));


        Ppu ppu = new Ppu();
        MemoryBus bus = new MemoryBus(ppu);

        CpuState state = new CpuState();
        Cpu cpu = new Cpu(bus, state);

        long nano = System.nanoTime();
        int thirdCycles = 0;
        while (cpu.tick()) {
            if (state.cycles >= SCANLINE_CPU_CYCLES) {
                ppu.runScanline();

                thirdCycles += 1;
                if (thirdCycles == 3) {
                    state.cycles++;
                    thirdCycles = 0;
                }
            }

            state.cycles = 0;
        }
    }
    
}
