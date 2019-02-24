package com.harryfreeborough.nesemu.instruction;

import com.harryfreeborough.nesemu.cpu.CpuMemory;
import com.harryfreeborough.nesemu.cpu.CpuState;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.harryfreeborough.nesemu.utils.MemoryUtils.setNZFlags;

/**
 * The implementation of an {@link Instruction}.
 */
public interface InstructionProcessor {

    static InstructionProcessor load(BiConsumer<CpuState, Integer> consumer) {
        return (memory, state, mode) -> {
            int value = mode.read1(memory, state);
            setNZFlags(state, value);
            consumer.accept(state, value);
        };
    }

    static InstructionProcessor compare(Function<CpuState, Integer> function) {
        return (memory, state, mode) -> {
            int value = mode.read1(memory, state);
            int register = function.apply(state);
            state.flagC = setNZFlags(state, register - value) >= 0;
        };
    }

    static InstructionProcessor branch(Predicate<CpuState> predicate) {
        return (memory, state, mode) -> {
            if (predicate.test(state)) {
                state.cycles += 2;
                if ((state.regPc & 0xFF00) != (state.regMar & 0xFF00)) {
                    state.cycles++;
                }
                state.regPc = state.regMar;
            }
        };
    }

    static InstructionProcessor combination(Instruction... instructions) {
        return (memory, state, mode) -> {
            for (Instruction instruction : instructions) {
                instruction.getProcessor().execute(memory, state, mode);
            }
        };
    }

    void execute(CpuMemory memory, CpuState state, AddressingMode mode);

}
