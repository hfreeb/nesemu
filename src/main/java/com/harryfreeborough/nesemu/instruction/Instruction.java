package com.harryfreeborough.nesemu.instruction;

import static com.harryfreeborough.nesemu.utils.MemoryUtils.setNZFlags;

import com.harryfreeborough.nesemu.utils.MemoryUtils;

/**
 * Represents all MOS 6502 instruction implementations.
 */
public enum Instruction {

    ADC((memory, state, mode) -> {
        int a = state.regA;
        int value = mode.read1(memory, state);
        int result = a + value + (state.flagC ? 1 : 0);

        state.flagC = (result >> 8) != 0;
        state.regA = setNZFlags(state, result & 0xFF);

        state.flagV = (((a ^ value) & 0x80) == 0) &&
                (((a ^ state.regA) & 0x80) != 0);
    }),
    SBC((memory, state, mode) -> {
        int a = state.regA;
        int value = mode.read1(memory, state);
        int result = a - value - (1 - (state.flagC ? 1 : 0));

        state.flagC = (result >> 8) == 0;
        state.regA = setNZFlags(state, result & 0xFF);

        state.flagV = (((a ^ value) & 0x80) != 0) &&
                (((a ^ state.regA) & 0x80) != 0);
    }),
    AND((memory, state, mode) -> state.regA = setNZFlags(state, state.regA & mode.read1(memory, state))),
    ORA((memory, state, mode) -> state.regA = setNZFlags(state, state.regA | mode.read1(memory, state))),
    EOR((memory, state, mode) -> state.regA = setNZFlags(state, state.regA ^ mode.read1(memory, state))),
    ROL((memory, state, mode) -> {
        int value = mode.read1(memory, state);
        int carry = state.flagC ? 1 : 0;
        state.flagC = (value >> 7) == 1;

        int result = ((value << 1) | carry) & 0xFF;
        mode.write1(memory, state, setNZFlags(state, result));
    }),
    ROR((memory, state, mode) -> {
        int value = mode.read1(memory, state);
        int carry = state.flagC ? 1 : 0;
        state.flagC = (value & 0x01) == 0x01;

        int result = (carry << 7) | (value >> 1);
        mode.write1(memory, state, setNZFlags(state, result));
    }),
    LDA((memory, state, mode) -> state.regA = setNZFlags(state, mode.read1(memory, state))),
    STA((memory, state, mode) -> mode.write1(memory, state, state.regA)),
    STX((memory, state, mode) -> mode.write1(memory, state, state.regX)),
    STY((memory, state, mode) -> mode.write1(memory, state, state.regY)),
    CMP(InstructionProcessor.compare(state -> state.regA)),
    CPX(InstructionProcessor.compare(state -> state.regX)),
    CPY(InstructionProcessor.compare(state -> state.regY)),

    BEQ(InstructionProcessor.branch(state -> state.flagZ)),
    BNE(InstructionProcessor.branch(state -> !state.flagZ)),
    BMI(InstructionProcessor.branch(state -> state.flagN)),
    BPL(InstructionProcessor.branch(state -> !state.flagN)),
    BCS(InstructionProcessor.branch(state -> state.flagC)),
    BCC(InstructionProcessor.branch(state -> !state.flagC)),
    BVS(InstructionProcessor.branch(state -> state.flagV)),
    BVC(InstructionProcessor.branch(state -> !state.flagV)),

    LDX(InstructionProcessor.load((state, value) -> state.regX = value)),
    LDY(InstructionProcessor.load((state, value) -> state.regY = value)),
    INY((memory, state, mode) -> state.regY = setNZFlags(state, (state.regY + 1) & 0xFF)),
    INX((memory, state, mode) -> state.regX = setNZFlags(state, (state.regX + 1) & 0xFF)),
    INC((memory, state, mode) -> mode.write1(memory, state, setNZFlags(state, (mode.read1(memory, state) + 1) & 0xFF))),
    JSR((memory, state, mode) -> {
        memory.stackPush2(state.regPc - 1, state);
        state.regPc = state.regMar;
    }),
    RTS((memory, state, mode) -> state.regPc = memory.stackPop2(state) + 1),
    CLC((memory, state, mode) -> state.flagC = false),
    SEC((memory, state, mode) -> state.flagC = true),
    CLI((memory, state, mode) -> state.flagI = false),
    SEI((memory, state, mode) -> state.flagI = true),
    CLV((memory, state, mode) -> state.flagV = false),
    SED((memory, state, mode) -> state.flagD = true),
    CLD((memory, state, mode) -> state.flagD = false),
    BIT((memory, state, mode) -> {
        int value = mode.read1(memory, state);
        state.flagZ = (value & state.regA) == 0;
        state.flagV = (value & (1 << 6)) != 0;
        state.flagN = (value & (1 << 7)) != 0;
    }),
    DEX((memory, state, mode) -> state.regX = setNZFlags(state, (state.regX - 1) & 0xFF)),
    DEY((memory, state, mode) -> state.regY = setNZFlags(state, (state.regY - 1) & 0xFF)),
    DEC((memory, state, mode) -> mode.write1(memory, state, setNZFlags(state, (mode.read1(memory, state) - 1) & 0xFF))),
    TAX((memory, state, mode) -> state.regX = setNZFlags(state, state.regA)),
    TAY((memory, state, mode) -> state.regY = setNZFlags(state, state.regA)),
    TXA((memory, state, mode) -> state.regA = setNZFlags(state, state.regX)),
    TYA((memory, state, mode) -> state.regA = setNZFlags(state, state.regY)),
    TXS(((memory, state, mode) -> state.regSp = state.regX)),
    TSX(((memory, state, mode) -> state.regX = setNZFlags(state, state.regSp))),
    LSR((memory, state, mode) -> {
        int old = mode.read1(memory, state);
        state.flagC = (old & 1) == 1;
        mode.write1(memory, state, setNZFlags(state, old >> 1));
    }),
    ASL((memory, state, mode) -> {
        int old = mode.read1(memory, state);
        state.flagC = (old & 0x80) != 0;
        int result = setNZFlags(state, (old << 1) & 0xFF);

        mode.write1(memory, state, result);
    }),
    PHA((memory, state, mode) -> memory.stackPush1(state.regA, state)),
    PHP((memory, state, mode) -> memory.stackPush1(state.getStatus(), state)),
    PLP((memory, state, mode) -> state.setStatus(memory.stackPop1(state))),
    PLA((memory, state, mode) -> state.regA = MemoryUtils.setNZFlags(state, memory.stackPop1(state))),
    JMP((memory, state, mode) -> state.regPc = state.regMar),
    BRK((memory, state, mode) -> {
        MemoryUtils.programPop1(memory, state); //Read next instruction byte and throw it away
        memory.stackPush2(state.regPc, state);
        memory.stackPush1(state.getStatus(), state);
        state.flagI = true;
        state.regPc = memory.read2(0xFFFE);
    }),
    RTI((memory, state, mode) -> {
        state.setStatus(memory.stackPop1(state));
        state.regPc = memory.stackPop2(state);
    }),
    NOP((memory, state, mode) -> { /* No operation */ }),
    /* Unofficial opcodes */
    RLA(InstructionProcessor.combination(ROL, AND));

    private final InstructionProcessor processor;

    Instruction(InstructionProcessor processor) {
        this.processor = processor;
    }

    public InstructionProcessor getProcessor() {
        return processor;
    }

}
