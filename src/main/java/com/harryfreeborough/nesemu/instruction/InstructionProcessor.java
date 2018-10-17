package com.harryfreeborough.nesemu.instruction;

import com.harryfreeborough.nesemu.Cpu;

public interface InstructionProcessor {

    void execute(Cpu cpu, AddressingMode mode);

}
