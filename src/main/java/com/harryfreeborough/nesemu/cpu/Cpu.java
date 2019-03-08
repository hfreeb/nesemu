package com.harryfreeborough.nesemu.cpu;

import com.harryfreeborough.nesemu.NesEmu;
import com.harryfreeborough.nesemu.instruction.AddressingMode;
import com.harryfreeborough.nesemu.instruction.Instruction;
import com.harryfreeborough.nesemu.instruction.Operation;
import com.harryfreeborough.nesemu.utils.MemoryUtils;
import com.harryfreeborough.nesemu.utils.Preconditions;

/**
 * Processes instructions.
 */
public class Cpu {

    private static Operation[] operations = new Operation[256];

    static {
        for (Operation operation : Operation.values()) {
            operations[operation.getOpcode()] = operation;
        }
    }

    private final CpuMemory memory;
    private CpuState state;

    public Cpu(CpuMemory memory) {
        this.memory = memory;

        this.state = new CpuState();
    }

    public Operation nextOperation() {
        int opcode = MemoryUtils.programPop1(this.memory, this.state);

        Operation operation = operations[opcode];
        Preconditions.checkNotNull(operation, "Failed to find instruction with id: $%02X", opcode);
        return operation;
    }


    /**
     * Executes the specified {@link Operation}.
     *
     * @param operation Operation to execute
     */
    public void tick(Operation operation) {
        Instruction instruction = operation.getInstruction();
        AddressingMode mode = operation.getAddressingMode();

        this.state.regMar = mode.obtainAddress(this.memory, this.state);
        instruction.getProcessor().execute(this.memory, this.state, mode);
        this.state.cycles += operation.getCycles();
    }

    /**
     * Raises a non-maskable interrupt.
     *
     * <p>Note: The interrupt handler address is read from $FFFA.</p>
     */
    public void raiseNmi() {
        this.memory.stackPush2(this.state.regPc, this.state);
        this.memory.stackPush1(this.state.getStatus(), this.state);

        this.state.regPc = this.memory.read2(0xFFFA);

        this.state.cycles += 7;
        this.state.flagI = true;
    }

    public CpuMemory getMemory() {
        return this.memory;
    }

    public CpuState getState() {
        return this.state;
    }


}
