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
    LDA((cpu, mode) -> {
        CpuState state = cpu.getState();
        int value = mode.read1(cpu);
        state.flagZ = value == 0;
        state.flagN = (value & 0b10000000) != 0;
        state.regA = value;
    }),
    STA(((cpu, mode) -> {
        mode.write1(cpu, cpu.getState().regA);
    })),
    CMP(InstructionProcessor.compare(state -> state.regA)),
    CPX(InstructionProcessor.compare(state -> state.regX)),
    CPY(InstructionProcessor.compare(state -> state.regY)),
    BNE((cpu, mode) -> {
        if (!cpu.getState().flagZ) {
            cpu.getState().regPc = mode.obtainAddress(cpu);
        } else {
            cpu.getState().regPc = (cpu.getState().regPc + 1) & 0xFFFF; //Skip arg
        }
    }),
    LDX(InstructionProcessor.load((state, value) -> state.regX = value)),
    LDY(InstructionProcessor.load((state, value) -> state.regY = value)),
    INY((cpu, mode) -> {
        CpuState state = cpu.getState();
        int value = (state.regY + 1) & 0xFF;
        state.flagZ = value == 0;
        state.flagN = (value & 0b10000000) != 0;
        state.regY = value;
    }),
    INX((cpu, mode) -> {
        CpuState state = cpu.getState();
        int value = (state.regX + 1) & 0xFF;
        state.flagZ = value == 0;
        state.flagN = (value & 0b10000000) != 0;
        state.regX = value;
    });

    private final InstructionProcessor processor;

    Instruction(InstructionProcessor processor) {
        this.processor = processor;
    }

    public InstructionProcessor getProcessor() {
        return processor;
    }

}
