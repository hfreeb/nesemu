package com.harryfreeborough.nesemu.instructions.execution;

import com.harryfreeborough.nesemu.MemoryBus;
import com.harryfreeborough.nesemu.CpuState;
import com.harryfreeborough.nesemu.addressing.InstructionMode;
import com.harryfreeborough.nesemu.instructions.Instruction;
import com.harryfreeborough.nesemu.utils.MemoryUtils;

/**
 * Jump, Saving Return address - Jump to execution and put
 * return address - 1 on the stack.
 */
public class InstructionJsr implements Instruction {

    @Override
    public int[] getOpCodes() {
        return new int[]{ 0x20 };
    }

    @Override
    public boolean obtainMode() {
        return false;
    }

    @Override
    public void execute(int opCode, InstructionMode mode, MemoryBus bus, CpuState store) {
        int subroutine = MemoryUtils.programPop2(bus, store);
        MemoryUtils.stackPush2(store.regPc - 1, bus, store);
        store.regPc = subroutine;
    }

}
