package com.harryfreeborough.nesemu;

import com.harryfreeborough.nesemu.device.MemoryBus;
import com.harryfreeborough.nesemu.instruction.AddressingMode;
import com.harryfreeborough.nesemu.instruction.Instruction;
import com.harryfreeborough.nesemu.instruction.Operation;
import com.harryfreeborough.nesemu.utils.MemoryUtils;

import java.util.Arrays;

public class Cpu {
    
    public static boolean running = true;
    
    private final MemoryBus bus;
    private final CpuState state;
    
    public Cpu(MemoryBus bus, CpuState state) {
        this.bus = bus;
        this.state = state;
        
        reset();
    }
    
    public boolean tick() {
        int sleeping = state.cycles / 20;
        try {
            Thread.sleep(sleeping);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        state.cycles -= sleeping * 20;
    
        /*System.out.println(String.format("A: $%02X, X: $%02X, Y: $%02X, S: $%02X, PC: $%04X, " +
                        "C: %b, N: %b, Z: %b, V: %b",
                state.regA, state.regX, state.regY, state.regSp, state.regPc,
                state.flagC, state.flagN, state.flagZ, state.flagV));
        for (int j = 0; j < 2; j++) {
            System.out.print(String.format("%04X: ", j));
            for (int k = 0; k < 16; k++) {
                System.out.print(String.format("%02X ", this.bus.read1(j * 16 + k)));
            }
            System.out.println();
        }*/
        
        int opcode = MemoryUtils.programPop1(this.bus, this.state);
        
        if (opcode == 0) {
            System.out.println("HALTING");
            running = false;
            return false;
        }
        
        Operation operation = Arrays.stream(Operation.values())
                .filter(i -> i.getOpcode() == opcode)
                .findAny()
                .orElseThrow(() -> new IllegalStateException(
                        String.format("Failed to find instruction with opcode: $%02X", opcode)
                ));
        Instruction instruction = operation.getInstruction();
        AddressingMode mode = operation.getAddressingMode();
        
        int arg1 = this.bus.read1(this.state.regPc);
        int arg2 = this.bus.read1(this.state.regPc + 1);
        
        String format = mode.getFormat()
                .replace("$[1,2]", String.format("$%04X", arg1 | (arg2 << 8)))
                .replace("$[1]", String.format("$%02X", arg1))
                .replace("[1]", String.format("%d", MemoryUtils.signedByteToInt(arg1)));
        
        System.out.println(String.format("Processing %s %s", instruction.name(), format));
        
        this.state.regMar = mode.obtainAddress(this.bus, this.state);
        instruction.getProcessor().execute(this.bus, this.state, mode);
        this.state.cycles += operation.getCycles();
        
        return true;
    }
    
    public void reset() {
        this.state.regPc = this.bus.read2(0xFFFC);
        this.state.regSp = 0xFD;
        
        this.state.flagC = false;
        this.state.flagZ = false;
        this.state.flagI = true;
        this.state.flagD = false;
        this.state.flagB = false;
        this.state.flagU = true;
        this.state.flagV = false;
        this.state.flagN = false;
    }
    
    public MemoryBus getBus() {
        return this.bus;
    }
    
    public CpuState getState() {
        return this.state;
    }
    
}
