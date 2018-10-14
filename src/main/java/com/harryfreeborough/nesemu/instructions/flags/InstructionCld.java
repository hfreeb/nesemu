package com.harryfreeborough.nesemu.instructions.flags;

import com.harryfreeborough.nesemu.MemoryBus;
import com.harryfreeborough.nesemu.CpuState;
import com.harryfreeborough.nesemu.addressing.InstructionMode;
import com.harryfreeborough.nesemu.instructions.Instruction;

/**
 * Clear Decimal Mode - set flagD to false.
 */
public class InstructionCld implements Instruction {

    @Override
    public int[] getOpCodes() {
        return new int[]{ 0xD8 };
    }

    @Override
    public boolean obtainMode() {
        return false;
    }

    @Override
    public void execute(int opCode, InstructionMode mode, MemoryBus bus, CpuState store) {
        store.flagD = false;
    }

}
