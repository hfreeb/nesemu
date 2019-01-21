package com.harryfreeborough.nesemu;

import com.harryfreeborough.nesemu.cpu.CpuMemory;
import com.harryfreeborough.nesemu.cpu.CpuState;
import com.harryfreeborough.nesemu.instruction.AddressingMode;
import com.harryfreeborough.nesemu.instruction.Instruction;
import com.harryfreeborough.nesemu.instruction.Operation;
import com.harryfreeborough.nesemu.ppu.PpuState;
import com.harryfreeborough.nesemu.utils.MemoryUtils;

public class DebugGen {

    private final Console console;
    
    public DebugGen(Console console) {
        this.console = console;
    }
    
    public String generate(Operation operation) {
        //TODO: Clean this all up
        Instruction instruction = operation.getInstruction();
        AddressingMode mode = operation.getAddressingMode();
        
        CpuMemory cpuMemory = this.console.getCpu().getMemory();
        CpuState cpuState = this.console.getCpu().getState();
        
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
        builder.append(" P:");
        builder.append(String.format("%02X", cpuState.regPc));
        builder.append(" SP:");
        builder.append(String.format("%02X", cpuState.regPc));
        builder.append(" PPU:");

        PpuState ppuState = this.console.getPpu().getState();

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

        builder.append(" v:");
        String v = Integer.toBinaryString(ppuState.regV);
        for (int i = v.length(); i < 15; i++) {
            builder.append("0");
        }
        builder.append(v);

        builder.append(" CYC:");
        builder.append(Integer.toString(cpuState.cycles));
        builder.append(" (");
        builder.append(cpuState.cycles / (114*260*60));
        builder.append("s)");
        
        return builder.toString();
    }
    
}
