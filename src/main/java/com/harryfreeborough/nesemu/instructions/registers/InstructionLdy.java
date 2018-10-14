package com.harryfreeborough.nesemu.instructions.registers;

import com.harryfreeborough.nesemu.MemoryBus;
import com.harryfreeborough.nesemu.CpuState;
import com.harryfreeborough.nesemu.addressing.InstructionMode;
import com.harryfreeborough.nesemu.instructions.Instruction;

public class InstructionLdy implements Instruction {

    @Override
    public int[] getOpCodes() {
        return new int[]{ 0xA0, 0xA4, 0xB4, 0xAC, 0xBC };
    }

    @Override
    public void execute(int opCode, InstructionMode mode, MemoryBus bus, CpuState store) {
        store.regY = mode.read1(bus, store);
    }

}
