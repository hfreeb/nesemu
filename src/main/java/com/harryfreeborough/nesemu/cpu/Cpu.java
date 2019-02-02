package com.harryfreeborough.nesemu.cpu;

import com.harryfreeborough.nesemu.NesEmu;
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

    private final CpuMemory bus;
    private CpuState state;

    public Cpu(CpuMemory bus) {
        this.bus = bus;

        this.state = new CpuState();
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

        NesEmu.DEBUGGER.process(operation);

        this.state.regMar = mode.obtainAddress(this.bus, this.state);
        instruction.getProcessor().execute(this.bus, this.state, mode);
        this.state.cycles += operation.getCycles();

        return true;
    }

    public void raiseNmi() {
        MemoryUtils.stackPush2(this.state.regPc, this.bus, this.state);
        MemoryUtils.stackPush1(this.state.getStatus(), this.bus, this.state);

        this.state.regPc = this.bus.read2(0xFFFA);
    }

    public CpuMemory getMemory() {
        return this.bus;
    }

    public CpuState getState() {
        return this.state;
    }


}
