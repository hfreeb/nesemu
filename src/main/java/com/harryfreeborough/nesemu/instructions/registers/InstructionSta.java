package com.harryfreeborough.nesemu.instructions.registers;

import com.harryfreeborough.nesemu.MemoryBus;
import com.harryfreeborough.nesemu.CpuState;
import com.harryfreeborough.nesemu.addressing.InstructionMode;
import com.harryfreeborough.nesemu.instructions.Instruction;

public class InstructionSta implements Instruction {

    @Override
    public int[] getOpCodes() {
        return new int[]{ 0x85, 0x95, 0x8D, 0x9D, 0x99, 0x81, 0x91 };
    }

    @Override
    public void execute(int opCode, InstructionMode mode, MemoryBus bus, CpuState store) {
        mode.write1(bus, store, store.regA);
    }

}
