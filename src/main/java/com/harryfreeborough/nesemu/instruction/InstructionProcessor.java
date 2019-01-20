package com.harryfreeborough.nesemu.instruction;

import com.harryfreeborough.nesemu.cpu.CpuState;
import com.harryfreeborough.nesemu.cpu.CpuMemory;
import com.harryfreeborough.nesemu.utils.MemoryUtils;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface InstructionProcessor {
    
    static InstructionProcessor load(BiConsumer<CpuState, Integer> consumer) {
        return (bus, state, mode) -> {
            int value = mode.read1(bus, state);
            MemoryUtils.setNZFlags(state, value);
            consumer.accept(state, value);
        };
    }
    
    static InstructionProcessor compare(Function<CpuState, Integer> function) {
        return (bus, state, mode) -> {
            int value = mode.read1(bus, state);
            int register = function.apply(state);
            state.flagZ = register == value;
            state.flagC = register >= value;
            state.flagN = (value & 1 << 7) != 0;
        };
    }
    
    static InstructionProcessor branch(Predicate<CpuState> predicate) {
        return (bus, state, mode ) -> {
            if (predicate.test(state)) {
                state.cycles += 2;
                if ((state.regPc & 0xFF00) != (state.regMar & 0xFF00)) {
                    state.cycles++;
                }
                state.regPc = state.regMar;
            }
        };
    }
    
    void execute(CpuMemory bus, CpuState state, AddressingMode mode);
    
}
