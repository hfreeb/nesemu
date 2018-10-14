package com.harryfreeborough.nesemu.instructions.registers;

import com.harryfreeborough.nesemu.MemoryBus;
import com.harryfreeborough.nesemu.CpuState;
import com.harryfreeborough.nesemu.addressing.InstructionMode;
import com.harryfreeborough.nesemu.instructions.Instruction;

public class InstructionIny implements Instruction {

    @Override
    public int[] getOpCodes() {
        return new int[] { 0xC8 };
    }

    @Override
    public void execute(int opCode, InstructionMode mode, MemoryBus bus, CpuState store) {
        store.regY++;
    }

}
