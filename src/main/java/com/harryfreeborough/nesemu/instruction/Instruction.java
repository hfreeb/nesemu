package com.harryfreeborough.nesemu.instruction;

import com.harryfreeborough.nesemu.CpuState;

public enum Instruction {

    ADC((cpu, mode) -> {
        CpuState state = cpu.getState();
        int value = mode.read1(cpu);
        int result = state.regA + value + (state.flagC ? 1 : 0);

        state.flagC = result >> 8 != 0;
        state.flagV = (((state.regA ^ value) & 0x80) == 0) &&
                (((state.regA ^ result) & 0x80) != 0);
        state.flagZ = result == 0;
        state.flagN = (result & 0b10000000) != 0;

        state.regA = result & 0xFF;
    }),
    LDA(((cpu, mode) -> {
        CpuState state = cpu.getState();
        int value = mode.read1(cpu);
        state.flagZ = value == 0;
        state.flagN = (value & 0b10000000) != 0;
        state.regA = value;
    })),
    CMP((cpu, mode) -> {
        CpuState state = cpu.getState();
        int value = mode.read1(cpu);
        state.flagZ = state.regA == value;
        state.flagC = state.regA >= value;
        state.flagN = (value & 0b10000000) != 0;
    }),
    BNE((cpu, mode) -> {
        if (!cpu.getState().flagZ) {
            cpu.getState().regPc += mode.read1(cpu) - 3;
        }
    });

    private final InstructionProcessor processor;

    Instruction(InstructionProcessor processor) {
        this.processor = processor;
    }

    public InstructionProcessor getProcessor() {
        return processor;
    }

}
