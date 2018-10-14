package com.harryfreeborough.nesemu.instructions.stack;

import com.harryfreeborough.nesemu.MemoryBus;
import com.harryfreeborough.nesemu.CpuState;
import com.harryfreeborough.nesemu.addressing.InstructionMode;
import com.harryfreeborough.nesemu.instructions.Instruction;

/**
 * Transfer index X to Stack pointer.
 */
public class InstructionTxs implements Instruction {

    @Override
    public int[] getOpCodes() {
        return new int[]{ 0x9A };
    }

    @Override
    public boolean obtainMode() {
        return false;
    }

    @Override
    public void execute(int opCode, InstructionMode mode, MemoryBus bus, CpuState store) {
        store.regS = store.regX;
    }

}
