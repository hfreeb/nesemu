package com.harryfreeborough.nesemu.instructions;

import com.harryfreeborough.nesemu.MemoryBus;
import com.harryfreeborough.nesemu.CpuState;
import com.harryfreeborough.nesemu.addressing.InstructionMode;

public interface Instruction {

    int[] getOpCodes();

    /**
     * @return Whether this instruction requires the {@link InstructionMode} to be obtained.
     */
    default boolean obtainMode() {
        return true;
    }

    void execute(int opCode, InstructionMode mode, MemoryBus bus, CpuState store);

}
