package com.harryfreeborough.nesemu;

import com.harryfreeborough.nesemu.cpu.Cpu;
import com.harryfreeborough.nesemu.rom.Cartridge;
import com.harryfreeborough.nesemu.rom.RomReader;
import com.harryfreeborough.nesemu.utils.Preconditions;

import java.nio.file.Paths;

public class NesEmu {

    public static final boolean DEBUG = System.getProperty("debug") != null;
    private static final int SCANLINE_CPU_CYCLES = 114; //263 / 3 TODO: should be 113 + 2/3

    public static void main(String[] args) {
        new NesEmu().run(args[0]);
    }
    
    public void run(String romPath) {
        Cartridge data = RomReader.read(Paths.get(romPath))
                .orElseThrow(() -> new RuntimeException("Failed to read rom."));
    
        Preconditions.checkState(data.getMapperId() == 0, "Only the NROM mapper is currently supported.");
    
        Console console = new Console(data);
        Cpu cpu = console.getCpu();

        int cycles = 0;
        while (cpu.tick()) {
            int diff = cpu.getState().cycles - cycles;
            for (int i = 0; i < diff; i++) {
                console.getPpu().tick();
            }
            
            cycles = cpu.getState().cycles;
        }
    }
    
}
