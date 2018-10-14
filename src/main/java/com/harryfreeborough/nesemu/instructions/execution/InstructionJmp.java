package com.harryfreeborough.nesemu.instructions.execution;

import com.harryfreeborough.nesemu.MemoryBus;
import com.harryfreeborough.nesemu.CpuState;
import com.harryfreeborough.nesemu.addressing.InstructionMode;
import com.harryfreeborough.nesemu.instructions.Instruction;
import com.harryfreeborough.nesemu.utils.MemoryUtils;

/**
 * JuMP to the specified address.
 */
public class InstructionJmp implements Instruction {

    @Override
    public int[] getOpCodes() {
        return new int[] { 0x4C, 0x6C };
    }

    @Override
    public boolean obtainMode() {
        //Ignore mode as this is JMP is a special case and doesn't follow the normal opcode structure for modes
        return false;
    }

    @Override
    public void execute(int opCode, InstructionMode mode, MemoryBus bus, CpuState store) {
        int address;
        if ((opCode & (1 << 5)) != 0) { //Indirect
            address = bus.read2(MemoryUtils.programPop2(bus, store));
        } else { //Absolute
            address = MemoryUtils.programPop2(bus, store);
        }

        store.regPc = address;
    }

}
