package com.harryfreeborough.nesemu;

import com.harryfreeborough.nesemu.cpu.Cpu;
import com.harryfreeborough.nesemu.instruction.Operation;
import com.harryfreeborough.nesemu.rom.Cartridge;
import com.harryfreeborough.nesemu.rom.MirroringMode;
import com.harryfreeborough.nesemu.rom.RomReader;
import com.harryfreeborough.nesemu.ui.EmuFrame;
import com.harryfreeborough.nesemu.utils.InputUtils;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Scanner;

public class NesEmu {

    private static final long FRAME_TIME = Math.floorDiv(1000, 30);

    public static void main(String[] args) {
        new NesEmu().run();
    }


    private final Debugger debugger = new Debugger();

    /**
     * Main logic loop.
     */
    public void run() {
        Cartridge cartridge = getCartridge().orElseThrow(() -> new RuntimeException("Failed to read rom."));
        Console console = new Console(cartridge);

        Cpu cpu = console.getCpu();

        EmuFrame frame = new EmuFrame(console);

        long lastFrame = 0;
        int cycles = 0;

        Optional<Integer> breakpoint = this.debugger.getTargetPc();
        while (true) {
            if (breakpoint.isPresent() && (breakpoint.get() == cpu.getState().regPc)) {
                System.out.println(String.format("BREAK at $%04X", cpu.getState().regPc));
                this.debugger.pause();
            }

            if (this.debugger.isPaused()) {
                boolean shouldExit = this.debugger.blockingCli(console);
                if (shouldExit) {
                    break;
                }
            }

            Operation operation = cpu.nextOperation();
            this.debugger.logOperation(console, operation);
            cpu.tick(operation);

            int catchup = (cpu.getState().cycles - cycles) * 3;

            for (int i = 0; i < catchup; i++) {
                boolean redraw = console.getPpu().tick();

                if (redraw) {
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

    /**
     * Reads the cartridge data using a file browser pop-up.
     * If the debug blank cartridge flag is set, a blank cartridge will be returned.
     *
     * @return optional {@link Cartridge}
     */
    private Optional<Cartridge> getCartridge() {
        String blankStart = System.getProperty(Debugger.BLANK_PROPERTY);
        //Mode to debug emulator by starting at specified address with blank cartridge
        if (blankStart != null) {
            byte[] prgRom = new byte[0x4000];
            //Set reset vector to 0x8000
            prgRom[0x3FFC] = 0;
            prgRom[0x3FFD] = (byte) 0x80;
            System.out.println("Enter data to write to program memory:");
            String input = new Scanner(System.in).nextLine();
            int i = 0;
            for (String b : input.split(" ")) {
                prgRom[i] = (byte) InputUtils.parseInteger(b);
                i++;
            }
            //Start paused to allow writing to program memory
            this.debugger.pause();
            return Optional.of(new Cartridge(MirroringMode.HORIZONTAL, false, 0,
                    prgRom.length / 0x4000, 1, null, prgRom, new byte[0x2000]));
        }

        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int jfcVal = jfc.showOpenDialog(null);
        if (jfcVal != JFileChooser.APPROVE_OPTION) {
            return Optional.empty();
        }

        InputStream stream;
        try {
            stream = new FileInputStream(jfc.getSelectedFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return Optional.empty();
        }

        return RomReader.read(stream);
    }

}
