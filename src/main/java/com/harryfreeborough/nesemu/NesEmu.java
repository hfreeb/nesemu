package com.harryfreeborough.nesemu;

import com.harryfreeborough.nesemu.cpu.Cpu;
import com.harryfreeborough.nesemu.rom.Cartridge;
import com.harryfreeborough.nesemu.rom.RomReader;
import com.harryfreeborough.nesemu.ui.EmuFrame;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Optional;

public class NesEmu {

    private static final long FRAME_TIME = Math.floorDiv(1000, 30);
    public static Debugger DEBUGGER;

    public static void main(String[] args) {
        try {
            new NesEmu().run();
        } finally {
            if (DEBUGGER != null) {
                DEBUGGER.write();
            }
        }
    }

    public void run() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int jfcVal = jfc.showOpenDialog(null);
        if (jfcVal != JFileChooser.APPROVE_OPTION) {
            return;
        }

        InputStream stream;
        try {
            stream = new FileInputStream(jfc.getSelectedFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        Cartridge data = RomReader.read(stream)
                .orElseThrow(() -> new RuntimeException("Failed to read rom."));


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
