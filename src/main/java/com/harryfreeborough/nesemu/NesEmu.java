package com.harryfreeborough.nesemu;

import com.harryfreeborough.nesemu.cpu.Cpu;
import com.harryfreeborough.nesemu.rom.Cartridge;
import com.harryfreeborough.nesemu.rom.RomReader;
import com.harryfreeborough.nesemu.ui.EmuFrame;
import com.harryfreeborough.nesemu.utils.Preconditions;

import javax.swing.*;
import java.nio.file.Paths;
import java.util.Optional;

public class NesEmu {

    public static Debugger DEBUGGER;
    
    private static final long FRAME_TIME = Math.floorDiv(1000, 30);

    public static void main(String[] args) {
        try {
            new NesEmu().run(args[0]);
        } catch (Exception ex) {
            DEBUGGER.write(); //A bit risky if the exception causes the debugger to not initialise
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public void run(String romPath) {
        Cartridge data = RomReader.read(Paths.get(romPath))
                .orElseThrow(() -> new RuntimeException("Failed to read rom."));

        Preconditions.checkState(data.getMapperId() == 0, "Only the NROM mapper is currently supported.");
        Preconditions.checkState(data.getChrRomSize() != 0, "CHR RAM is not supported.");
        Preconditions.checkState(data.getPrgRomSize() != 0, "PRG RAM is not supported.");

        
        Console console = new Console(data);
        DEBUGGER = new Debugger(console);
        
        Cpu cpu = console.getCpu();

        EmuFrame frame = new EmuFrame(console);

        long lastFrame = 0;
        int cycles = 0;
        
        Optional<Integer> breakpoint = DEBUGGER.getTargetPc();
        while (true) {
            if (breakpoint.isPresent() && (breakpoint.get() == cpu.getState().regPc)) {
                System.out.println(String.format("BREAK at $%04X", cpu.getState().regPc));
                DEBUGGER.pause();
            }
            
            if (DEBUGGER.isPaused()) {
                if (!DEBUGGER.processPause()) {
                    continue;
                }
            }
            
            //Break on halt
            if (!cpu.tick()) {
                DEBUGGER.pause();
                continue;
            }
            
            int catchup = (cpu.getState().cycles - cycles) * 3;

            for (int i = 0; i < catchup; i++) {
                if (console.getPpu().tick()) {
                    SwingUtilities.invokeLater(() -> {
                        frame.validate();
                        frame.repaint();
                    });

                    long now = System.currentTimeMillis();
                    long delta = now - lastFrame;
                    if (delta < FRAME_TIME) {
                        try {
                            Thread.sleep(FRAME_TIME - delta);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    lastFrame = now;
                }
            }

            cycles = cpu.getState().cycles;
        }
    }

}
