package com.harryfreeborough.nesemu.instructions.bitwise;

import com.harryfreeborough.nesemu.MemoryBus;
import com.harryfreeborough.nesemu.CpuState;
import com.harryfreeborough.nesemu.addressing.InstructionMode;
import com.harryfreeborough.nesemu.instructions.Instruction;

public class InstructionAsl implements Instruction {

    @Override
    public int[] getOpCodes() {
        return new int[]{ 0x0A, 0x06, 0x16, 0x0E, 0x1E };
    }

    @Override
    public void execute(int opCode, InstructionMode mode, MemoryBus bus, CpuState store) {
        int value = mode.read1(bus, store);
        mode.write1(bus, store, (value << 1) & 0xFF);
    }

}
