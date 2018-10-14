package com.harryfreeborough.nesemu.instructions.arithmetic;

import com.harryfreeborough.nesemu.MemoryBus;
import com.harryfreeborough.nesemu.CpuState;
import com.harryfreeborough.nesemu.addressing.InstructionMode;
import com.harryfreeborough.nesemu.instructions.Instruction;

/**
 * ADd with Carry.
 */
public class InstructionAdc implements Instruction {

    @Override
    public int[] getOpCodes() {
        return new int[]{ 0x69, 0x65, 0x75, 0x6D, 0x7D, 0x79, 0x61, 0x71 };
    }

    @Override
    public void execute(int opCode, InstructionMode mode, MemoryBus bus, CpuState store) {
        store.regA = (store.regA + mode.read1(bus, store)) & 0xFF;
    }

}
