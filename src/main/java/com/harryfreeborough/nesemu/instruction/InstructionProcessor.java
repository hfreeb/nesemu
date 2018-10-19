package com.harryfreeborough.nesemu.instruction;

import com.harryfreeborough.nesemu.Cpu;
import com.harryfreeborough.nesemu.CpuState;

import java.util.function.BiConsumer;
import java.util.function.Function;

public interface InstructionProcessor {

    void execute(Cpu cpu, AddressingMode mode);

    static InstructionProcessor load(BiConsumer<CpuState, Integer> consumer) {
        return (cpu, mode) -> {
            CpuState state = cpu.getState();
            int value = mode.read1(cpu);
            state.flagZ = value == 0;
            state.flagN = (value & 0b10000000) != 0;
            consumer.accept(state, value);
        };
    }

    static InstructionProcessor compare(Function<CpuState, Integer> function) {
        return (cpu, mode) -> {
            CpuState state = cpu.getState();
            int value = mode.read1(cpu);
            int register = function.apply(state);
            state.flagZ = register == value;
            state.flagC = register >= value;
            state.flagN = (value & 0b10000000) != 0;
        };
    }

}
