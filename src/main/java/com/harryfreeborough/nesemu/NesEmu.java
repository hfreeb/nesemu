package com.harryfreeborough.nesemu;

import com.harryfreeborough.nesemu.cpu.Cpu;
import com.harryfreeborough.nesemu.rom.Cartridge;
import com.harryfreeborough.nesemu.rom.RomReader;
import com.harryfreeborough.nesemu.ui.EmuFrame;
import com.harryfreeborough.nesemu.utils.Preconditions;

import javax.swing.*;
import java.nio.file.Paths;

public class NesEmu {

    public static boolean DEBUG = System.getProperty("debug") != null;
    private static final long FRAME_TIME = Math.floorDiv(1000, 60);

    public static void main(String[] args) {
        new NesEmu().run(args[0]);
    }

    public void run(String romPath) {
        Cartridge data = RomReader.read(Paths.get(romPath))
                .orElseThrow(() -> new RuntimeException("Failed to read rom."));

        Preconditions.checkState(data.getMapperId() == 0, "Only the NROM mapper is currently supported.");
        Preconditions.checkState(data.getChrRomSize() != 0, "CHR RAM is not supported.");

        Console console = new Console(data);
        Cpu cpu = console.getCpu();

        EmuFrame frame = new EmuFrame(console);

        long lastFpsReport = 0;
        int frames = 0;

        long lastFrame = 0;
        int cycles = 0;
        while (cpu.tick()) {
            int catchup = (cpu.getState().cycles - cycles) * 3;

            for (int i = 0; i < catchup; i++) {
                if (console.getPpu().tick()) {
                    SwingUtilities.invokeLater(() -> {
                        frame.validate();
                        frame.repaint();
                    });

                    /*
                                       long now = System.currentTimeMillis();
                    long delta = now - lastFrame;
                    if (delta < FRAME_TIME) {
                        try {
                            Thread.sleep(FRAME_TIME - delta);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    lastFrame = now;*/
                }
            }

            cycles = cpu.getState().cycles;
        }
    }

}
