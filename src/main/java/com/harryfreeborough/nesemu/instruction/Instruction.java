package com.harryfreeborough.nesemu.instruction;

import com.harryfreeborough.nesemu.utils.MemoryUtils;

import static com.harryfreeborough.nesemu.utils.MemoryUtils.setNZFlags;

public enum Instruction {
    
    ADC((bus, state, mode) -> {
        int a = state.regA;
        int value = mode.read1(bus, state);
        int result = a + value + (state.flagC ? 1 : 0);
        
        state.flagC = (result >> 8) != 0;
        state.regA = setNZFlags(state, result & 0xFF);
        
        state.flagV = (((a ^ value) & 0x80) == 0) &&
                (((a ^ state.regA) & 0x80) != 0);
    }),
    SBC((bus, state, mode) -> {
        int a = state.regA;
        int value = mode.read1(bus, state);
        int result = a - value - (1 - (state.flagC ? 1 : 0));
        
        state.flagC = (result >> 8) == 0;
        state.regA = setNZFlags(state, result & 0xFF);
        
        state.flagV = (((a ^ value) & 0x80) != 0) &&
                (((a ^ state.regA) & 0x80) != 0);
    }),
    AND((bus, state, mode) -> state.regA = setNZFlags(state, state.regA & mode.read1(bus, state))),
    ORA((bus, state, mode) -> state.regA = setNZFlags(state, state.regA | mode.read1(bus, state))),
    EOR((bus, state, mode) -> state.regA = setNZFlags(state, state.regA ^ mode.read1(bus, state))),
    ROL((bus, state, mode) -> {
        int value = mode.read1(bus, state);
        int carry = state.flagC ? 1 : 0;
        state.flagC = (value >> 7) == 1;
        
        int result = ((value << 1) | carry) & 0xFF;
        mode.write1(bus, state, setNZFlags(state, result));
    }),
    ROR((bus, state, mode) -> {
        int value = mode.read1(bus, state);
        int carry = state.flagC ? 1 : 0;
        state.flagC = (value & 0x01) == 0x01;
        
        int result = (carry << 7) | (value >> 1);
        mode.write1(bus, state, setNZFlags(state, result));
    }),
    LDA((bus, state, mode) -> state.regA = setNZFlags(state, mode.read1(bus, state))),
    STA((bus, state, mode) -> mode.write1(bus, state, state.regA)),
    STX((bus, state, mode) -> mode.write1(bus, state, state.regX)),
    STY((bus, state, mode) -> mode.write1(bus, state, state.regY)),
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
    INY((bus, state, mode) -> state.regY = setNZFlags(state, (state.regY + 1) & 0xFF)),
    INX((bus, state, mode) -> state.regX = setNZFlags(state, (state.regX + 1) & 0xFF)),
    INC((bus, state, mode) -> mode.write1(bus, state, setNZFlags(state, (mode.read1(bus, state) + 1) & 0xFF))),
    JSR((bus, state, mode) -> {
        MemoryUtils.stackPush2(state.regPc - 1, bus, state);
        state.regPc = state.regMar;
    }),
    RTS((bus, state, mode) -> state.regPc = MemoryUtils.stackPop2(bus, state) + 1),
    CLC((bus, state, mode) -> state.flagC = false),
    SEC((bus, state, mode) -> state.flagC = true),
    CLI((bus, state, mode) -> state.flagI = false),
    SEI((bus, state, mode) -> state.flagI = true),
    CLV((bus, state, mode) -> state.flagV = false),
    SED((bus, state, mode) -> state.flagD = true),
    CLD((bus, state, mode) -> state.flagD = false),
    BIT((bus, state, mode) -> {
        int value = mode.read1(bus, state);
        state.flagZ = (value & state.regA) == 0;
        state.flagV = (value & (1 << 6)) != 0;
        state.flagN = (value & (1 << 7)) != 0;
    }),
    DEX((bus, state, mode) -> state.regX = setNZFlags(state, (state.regX - 1) & 0xFF)),
    DEY((bus, state, mode) -> state.regY = setNZFlags(state, (state.regY - 1) & 0xFF)),
    DEC((bus, state, mode) -> mode.write1(bus, state, setNZFlags(state, (mode.read1(bus, state) - 1) & 0xFF))),
    TAX((bus, state, mode) -> state.regX = setNZFlags(state, state.regA)),
    TAY((bus, state, mode) -> state.regY = setNZFlags(state, state.regA)),
    TXA((bus, state, mode) -> state.regA = setNZFlags(state, state.regX)),
    TYA((bus, state, mode) -> state.regA = setNZFlags(state, state.regY)),
    TXS(((bus, state, mode) -> state.regSp = state.regX)),
    TSX(((bus, state, mode) -> state.regX = setNZFlags(state, state.regSp))),
    LSR((bus, state, mode) -> {
        int old = mode.read1(bus, state);
        state.flagC = (old & 1) == 1;
        mode.write1(bus, state, setNZFlags(state, old >> 1));
    }),
    ASL((bus, state, mode) -> {
        int old = mode.read1(bus, state);
        state.flagC = (old & 0x80) != 0;
        int result = setNZFlags(state, (old << 1) & 0xFF);
        
        mode.write1(bus, state, result);
    }),
    PHA((bus, state, mode) -> MemoryUtils.stackPush1(state.regA, bus, state)),
    PHP((bus, state, mode) -> MemoryUtils.stackPush1(state.getStatus() | 0x10, bus, state)), //TODO: Check | 0x10
    PLP((bus, state, mode) -> state.setStatus(MemoryUtils.stackPop1(bus, state))),
    PLA((bus, state, mode) -> state.regA = MemoryUtils.setNZFlags(state, MemoryUtils.stackPop1(bus, state))),
    JMP((bus, state, mode) -> state.regPc = state.regMar),
    RTI((bus, state, mode) -> {
        state.setStatus(MemoryUtils.stackPop1(bus, state));
        state.regPc = MemoryUtils.stackPop2(bus, state);
    }),
    NOP((bus, state, mode) -> { /* NOP */ }),
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
