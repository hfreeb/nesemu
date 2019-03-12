package com.harryfreeborough.nesemu;

import com.harryfreeborough.nesemu.cpu.CpuMemory;
import com.harryfreeborough.nesemu.cpu.CpuState;
import com.harryfreeborough.nesemu.instruction.AddressingMode;
import com.harryfreeborough.nesemu.instruction.Instruction;
import com.harryfreeborough.nesemu.instruction.Operation;
import com.harryfreeborough.nesemu.ppu.PpuState;
import com.harryfreeborough.nesemu.utils.MemoryUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Scanner;

import static com.harryfreeborough.nesemu.utils.InputUtils.parseInteger;

/**
 * Handles all debugging features of the emulator.
 */
public class Debugger {

    public static String BLANK_PROPERTY = "debug-blank";

    private final static Integer BREAKPOINT;
    private static boolean LOG = System.getProperty("debug-log") != null;

    static {
        String value = System.getProperty("debug-target-pc");
        if (value != null) {
            BREAKPOINT = Integer.parseInt(value, 16);
        } else {
            BREAKPOINT = null;
        }
    }

    private final Scanner scanner;
    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;
    //Start paused if using blank cartridge
    private boolean paused;

    public Debugger() {
        this.scanner = new Scanner(System.in);

        try {
            this.fileWriter = new FileWriter(Paths.get("latest.log").toFile());
            this.bufferedWriter = new BufferedWriter(this.fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Optional<Integer> getTargetPc() {
        return Optional.ofNullable(BREAKPOINT);
    }

    public boolean isPaused() {
        return this.paused;
    }

    public void pause() {
        this.paused = true;
    }

    /**
     * Runs the debugger command line interface, blocking the current thread.
     *
     * @return whether the program should exit
     */
    public boolean blockingCli(Console console) {
        while (true) {
            System.out.print("(Debug) ");

            String[] input = this.scanner.nextLine().split(" ");

            switch (input[0]) {
                case "help":
                    print("-- Help --");
                    print("step              - Step a single instruction.");
                    print("continue          - Continues execution.");
                    print("r1 {addr}         - Read a single byte from the specified address");
                    print("r2 {addr}         - Read two bytes from the specified address");
                    print("w1 {addr} {value} - Writes a single byte to the specified address.");
                    print("w2 {addr} {value} - Writes two bytes to the specified address.");
                    print("quit              - Quits the program.");
                    break;
                case "s":
                case "step":
                    return false;
                case "r1":
                    if (input.length < 2) {
                        invalidSyntax("r1 {addr}");
                    } else {
                        int value = console.getCpu().getMemory().read1(parseInteger(input[1]));
                        System.out.println(String.format("$%02X", value));
                    }
                    break;
                case "r2":
                    if (input.length < 2) {
                        invalidSyntax("r2 {addr}");
                    } else {
                        int value = console.getCpu().getMemory().read2(parseInteger(input[1]));
                        System.out.println(String.format("$%04X", value));
                    }
                    break;
                case "w1":
                    if (input.length < 3) {
                        invalidSyntax("w1 {addr} {value}");
                    } else {
                        int address = parseInteger(input[1]);
                        int value = parseInteger(input[2]);

                        console.getCpu().getMemory().write1(address, value);
                    }
                    break;
                case "w2":
                    if (input.length < 3) {
                        invalidSyntax("w1 {addr} {value}");
                    } else {
                        int address = parseInteger(input[1]);
                        int value = parseInteger(input[2]);

                        console.getCpu().getMemory().write2(address, value);
                    }
                    break;
                case "c":
                case "continue":
                    this.paused = false;
                    return false;
                case "q":
                case "quit":
                    return true;
                default:
                    print("Invalid command: %s. Try \"help\".", input[0]);
                    break;
            }
        }
    }

    private void print(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

    private void invalidSyntax(String syntax) {
        print("Invalid syntax. Try \"%s\".", syntax);
    }

    public void logOperation(Console console, Operation operation) {
        if (!LOG && !paused) {
            return;
        }

        Instruction instruction = operation.getInstruction();
        AddressingMode mode = operation.getAddressingMode();

        CpuMemory cpuMemory = console.getCpu().getMemory();
        CpuState cpuState = console.getCpu().getState();

        int argsLength = 0;
        String arguments = mode.getFormat();
        if (arguments.contains("$[1,2]")) {
            argsLength = 2;
            int arg1 = cpuMemory.read1(cpuState.regPc);
            int arg2 = cpuMemory.read1(cpuState.regPc + 1);
            arguments = arguments.replace("$[1,2]", String.format("$%04X", arg1 | (arg2 << 8)));
        } else if (arguments.contains("[1]")) {
            argsLength = 1;
            int arg = cpuMemory.read1(cpuState.regPc);
            arguments = arguments.replace("$[1]", String.format("$%02X", arg))
                    .replace("[1]", String.format("%d", MemoryUtils.signedByteToInt(arg)));
        }

        //TODO: Create function to convert number to n long padded out hex, should speed it up
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%04X", cpuState.regPc - 1));
        builder.append(" ");
        for (int i = 0; i < 3; i++) {
            if (i < (argsLength + 1)) {
                builder.append(String.format("%02X ", cpuMemory.read1(cpuState.regPc - 1 + i)));
            } else {
                builder.append("   ");
            }
        }
        builder.append(instruction.name());
        builder.append(" ");
        builder.append(arguments);

        for (int i = builder.length(); i < 48; i++) {
            builder.append(" ");
        }

        builder.append("A:");
        builder.append(String.format("%02X", cpuState.regA));
        builder.append(" X:");
        builder.append(String.format("%02X", cpuState.regX));
        builder.append(" Y:");
        builder.append(String.format("%02X", cpuState.regY));
        builder.append(" SP:");
        builder.append(String.format("%02X", cpuState.regSp));

        builder.append(" S:");
        String status = Integer.toBinaryString(cpuState.getStatus());
        for (int i = status.length(); i < 8; i++) {
            builder.append("0");
        }
        builder.append(status);

        builder.append(" PPU:");
        PpuState ppuState = console.getPpu().getState();
        String scanline = Integer.toString(ppuState.scanline);
        for (int i = scanline.length(); i < 3; i++) {
            builder.append(" ");
        }
        builder.append(scanline);
        String dot = Integer.toString(ppuState.dot);
        for (int i = dot.length(); i < 3; i++) {
            builder.append(" ");
        }
        builder.append("(");
        builder.append(dot);
        builder.append(")");

        builder.append(" CYC:");
        builder.append(Integer.toString(cpuState.cycles));
        builder.append(" (");
        builder.append(Math.floorDiv(cpuState.cycles, 114 * 260 * 60));
        builder.append("s)");

        if (this.paused) {
            System.out.println(builder.toString());
        }

        builder.append("\n");

        if (LOG) {
            try {
                this.bufferedWriter.append(builder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void write() {
        try {
            this.bufferedWriter.close();
            this.fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
