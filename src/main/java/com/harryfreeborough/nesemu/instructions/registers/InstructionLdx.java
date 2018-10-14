package com.harryfreeborough.nesemu.instructions.registers;

import com.harryfreeborough.nesemu.MemoryBus;
import com.harryfreeborough.nesemu.CpuState;
import com.harryfreeborough.nesemu.addressing.InstructionMode;
import com.harryfreeborough.nesemu.instructions.Instruction;

public class InstructionLdx implements Instruction {

    @Override
    public int[] getOpCodes() {
        return new int[]{ 0xA2, 0xA6, 0xB6, 0xAE, 0xBE };
    }

    @Override
    public void execute(int opCode, InstructionMode mode, MemoryBus bus, CpuState store) {
        store.regX = mode.read1(bus, store);
    }
}
