package com.harryfreeborough.nesemu;

import com.harryfreeborough.nesemu.device.Memory;
import com.harryfreeborough.nesemu.device.MemoryBus;
import com.harryfreeborough.nesemu.device.MemoryMirror;
import com.harryfreeborough.nesemu.ppu.Ppu;
import com.harryfreeborough.nesemu.ppu.PpuDevice;
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
    
        MemoryBus bus = new MemoryBus();
        bus.registerDevice(new Memory(0x0800, false), 0x0000);
        bus.registerDevice(new MemoryMirror(bus, 0x1800, 0x0000, 0x0800), 0x0800);
        bus.registerDevice(new PpuDevice(), 0x2000);
    
        //data.getTrainerData().ifPresent(bytes -> bus.registerDevice(new Memory(bytes, true), 0x7000));
        bus.registerDevice(new Memory(data.getPrgRomData(), true), 0x8000);
        //Assumes NES-128 where there is only one PRG ROM bank, as 16KiB mapped and then mirrored to 0xC000
        //TODO: remove these assumptions
        bus.registerDevice(new MemoryMirror(bus, 0x4000, 0x8000, 0x4000), 0xC000);

        CpuState state = new CpuState();
        Cpu cpu = new Cpu(bus, state);
        Ppu ppu = new Ppu();

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
