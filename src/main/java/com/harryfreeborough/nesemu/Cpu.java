package com.harryfreeborough.nesemu;

import com.harryfreeborough.nesemu.instruction.AddressingMode;
import com.harryfreeborough.nesemu.instruction.Instruction;
import com.harryfreeborough.nesemu.instruction.Operation;
import com.harryfreeborough.nesemu.utils.MemoryUtils;
import com.harryfreeborough.nesemu.utils.Preconditions;

import java.util.HashMap;
import java.util.Map;

public class Cpu {
    
    private static Map<Integer, Operation> operations;
    
    static {
        operations = new HashMap<>();
        for (Operation operation : Operation.values()) {
            operations.put(operation.getOpcode(), operation);
        }
    }
    
    private final MemoryBus bus;
    private final CpuState state;
    private final CpuDebug debug;
    
    public Cpu(MemoryBus bus, CpuState state, CpuDebug debug) {
        this.bus = bus;
        this.state = state;
        this.debug = debug;
        
        reset();
    }
    
    public boolean tick() {
        int opcode = MemoryUtils.programPop1(this.bus, this.state);
        
        if (opcode == 0) {
            System.out.println("HALTING");
            return false;
        }
        
        Operation operation = operations.get(opcode);
        Preconditions.checkNotNull(operation, "Failed to find instruction with id: $%02X", opcode);
        
        Instruction instruction = operation.getInstruction();
        AddressingMode mode = operation.getAddressingMode();
        
        if (NesEmu.DEBUG) {
            this.debug.run(operation);
        }
        
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
