package com.harryfreeborough.nesemu.instructions.bitwise;

import com.harryfreeborough.nesemu.MemoryBus;
import com.harryfreeborough.nesemu.CpuState;
import com.harryfreeborough.nesemu.addressing.InstructionMode;
import com.harryfreeborough.nesemu.instructions.Instruction;

/**
 * Bitwise AND instruction.
 */
public class InstructionAnd implements Instruction {

    @Override
    public int[] getOpCodes() {
        return new int[]{ 0x29, 0x25, 0x35, 0x2D, 0x3D, 0x39, 0x21, 0x31 };
    }

    @Override
    public void execute(int opCode, InstructionMode mode, MemoryBus bus, CpuState store) {
        store.regA &= mode.read1(bus, store);
    }
}
