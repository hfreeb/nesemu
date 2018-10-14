package com.harryfreeborough.nesemu.instructions.registers;

import com.harryfreeborough.nesemu.MemoryBus;
import com.harryfreeborough.nesemu.CpuState;
import com.harryfreeborough.nesemu.addressing.InstructionMode;
import com.harryfreeborough.nesemu.instructions.Instruction;

/**
 * LoaDs Accumulator with a value from memory.
 */
public class InstructionLda implements Instruction {

    @Override
    public int[] getOpCodes() {
        return new int[]{ 0xA9, 0xA5, 0xB5, 0xAD, 0xBD, 0xB9, 0xA1, 0xB1 };
    }

    @Override
    public void execute(int opCode, InstructionMode mode, MemoryBus bus, CpuState store) {
        store.regA = mode.read1(bus, store);
    }

}
