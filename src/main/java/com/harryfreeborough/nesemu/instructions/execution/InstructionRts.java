package com.harryfreeborough.nesemu.instructions.execution;

import com.harryfreeborough.nesemu.MemoryBus;
import com.harryfreeborough.nesemu.CpuState;
import com.harryfreeborough.nesemu.addressing.InstructionMode;
import com.harryfreeborough.nesemu.instructions.Instruction;
import com.harryfreeborough.nesemu.utils.MemoryUtils;

public class InstructionRts implements Instruction {

    @Override
    public int[] getOpCodes() {
        return new int[]{ 0x60 };
    }

    @Override
    public boolean obtainMode() {
        return false;
    }

    @Override
    public void execute(int opCode, InstructionMode mode, MemoryBus bus, CpuState store) {
        store.regPc = MemoryUtils.stackPop2(bus, store) + 1;
    }

}
