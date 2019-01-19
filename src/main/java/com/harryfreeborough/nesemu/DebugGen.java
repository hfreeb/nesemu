package com.harryfreeborough.nesemu;

import com.harryfreeborough.nesemu.cpu.CpuMemory;
import com.harryfreeborough.nesemu.cpu.CpuState;
import com.harryfreeborough.nesemu.instruction.AddressingMode;
import com.harryfreeborough.nesemu.instruction.Instruction;
import com.harryfreeborough.nesemu.instruction.Operation;
import com.harryfreeborough.nesemu.ppu.PpuState;
import com.harryfreeborough.nesemu.utils.MemoryUtils;

public class DebugGen {
    
    private final CpuState cpuState;
    private final PpuState ppuState;
    private final CpuMemory bus;
    
    public DebugGen(CpuState cpuState, PpuState ppuState, CpuMemory bus) {
        this.cpuState = cpuState;
        this.ppuState = ppuState;
        this.bus = bus;
    }
    
    public String generate(Operation operation) {
        //TODO: Clean this all up
        Instruction instruction = operation.getInstruction();
        AddressingMode mode = operation.getAddressingMode();
        
        int argsLength = 0;
        String arguments = mode.getFormat();
        if (arguments.contains("$[1,2]")) {
            argsLength = 2;
            int arg1 = this.bus.read1(this.cpuState.regPc);
            int arg2 = this.bus.read1(this.cpuState.regPc + 1);
            arguments = arguments.replace("$[1,2]", String.format("$%04X", arg1 | (arg2 << 8)));
        } else if (arguments.contains("[1]")) {
            argsLength = 1;
            int arg = this.bus.read1(this.cpuState.regPc);
            arguments = arguments.replace("$[1]", String.format("$%02X", arg))
                                 .replace("[1]", String.format("%d", MemoryUtils.signedByteToInt(arg)));
        }
    
        //TODO: Create function to convert number to n long padded out hex, should speed it up
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%04X", this.cpuState.regPc));
        builder.append(" ");
        for (int i = 0; i < 3; i++) {
            if (i < (argsLength + 1)) {
                builder.append(String.format("%02X ", this.bus.read1(this.cpuState.regPc - 1 + i)));
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
        builder.append(String.format("%02X", this.cpuState.regA));
        builder.append(" X:");
        builder.append(String.format("%02X", this.cpuState.regX));
        builder.append(" Y:");
        builder.append(String.format("%02X", this.cpuState.regY));
        builder.append(" P:");
        builder.append(String.format("%02X", this.cpuState.regPc));
        builder.append(" SP:");
        builder.append(String.format("%02X", this.cpuState.regPc));
        builder.append(" PPU:");
        
        String scanline = Integer.toString(this.ppuState.scanline);
        for (int i = scanline.length(); i < 3; i++) {
            builder.append(" ");
        }
        builder.append(scanline);
        
        builder.append(" CYC:");
        builder.append(Integer.toString(this.cpuState.cycles));
        builder.append(" (");
        builder.append(this.cpuState.cycles / (114*260*60));
        builder.append("s)");
        
        return builder.toString();
    }
    
}
