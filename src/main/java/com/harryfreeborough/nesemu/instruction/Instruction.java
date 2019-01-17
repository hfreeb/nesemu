package com.harryfreeborough.nesemu.instruction;

import com.harryfreeborough.nesemu.utils.MemoryUtils;

import static com.harryfreeborough.nesemu.utils.MemoryUtils.setNZFlags;

public enum Instruction {
    
    ADC((bus, state, mode) -> {
        int value = mode.read1(bus, state);
        int result = state.regA + value + (state.flagC ? 1 : 0);
        
        state.flagC = (result >> 8) != 0;
        state.flagV = (((state.regA ^ value) & 0x80) == 0) &&
                (((state.regA ^ result) & 0x80) != 0);
        setNZFlags(state, result);
        
        state.regA = result & 0xFF;
    }),
    SBC((bus, state, mode) -> {
        int value = mode.read1(bus, state);
        int result = state.regA - value + (state.flagC ? 1 : 0);
    
        state.flagC = (result >> 8) == 0;
        state.flagV = ((state.regA ^ result) & (value ^ result) & 0x80) == 0x80;
        state.flagV = (((state.regA ^ value) & 0x80) == 0) &&
                (((state.regA ^ result) & 0x80) != 0);
        setNZFlags(state, result);
    
        state.regA = result & 0xFF;
    }),
    AND((bus, state, mode) -> state.regA = setNZFlags(state, state.regA & mode.read1(bus, state))),
    LDA((bus, state, mode) -> state.regA = setNZFlags(state, mode.read1(bus, state))),
    STA((bus, state, mode) -> mode.write1(bus, state, state.regA)),
    CMP(InstructionProcessor.compare(state -> state.regA)),
    CPX(InstructionProcessor.compare(state -> state.regX)),
    CPY(InstructionProcessor.compare(state -> state.regY)),
    
    BNE(InstructionProcessor.branch(state -> !state.flagZ)),
    BEQ(InstructionProcessor.branch(state -> state.flagZ)),
    BPL(InstructionProcessor.branch(state -> !state.flagN)),
    BCS(InstructionProcessor.branch(state -> state.flagC)),
    BCC(InstructionProcessor.branch(state -> !state.flagC)),
    
    LDX(InstructionProcessor.load((state, value) -> state.regX = value)),
    LDY(InstructionProcessor.load((state, value) -> state.regY = value)),
    INY((bus, state, mode) -> state.regY = setNZFlags(state, (state.regY + 1) & 0xFF)),
    INX((bus, state, mode) -> state.regX = setNZFlags(state, (state.regX + 1) & 0xFF)),
    INC((bus, state, mode) -> mode.write1(bus, state, setNZFlags(state, (mode.read1(bus, state) + 1) & 0xFF))),
    JSR((bus, state, mode) -> {
        MemoryUtils.stackPush2(state.regPc - 1, bus, state);
        state.regPc = state.regMar;
    }),
    RTS((bus, state, mode) -> state.regPc = MemoryUtils.stackPop2(bus, state) + 1 ),
    CLC((bus, state, mode) -> state.flagC = false),
    SEC((bus, state, mode) -> state.flagC = true),
    CLI((bus, state, mode) -> state.flagI = false),
    SEI((bus, state, mode) -> state.flagI = true),
    CLV((bus, state, mode) -> state.flagV = false),
    CLD((bus, state, mode) -> state.flagD = false),
    BIT((bus, state, mode) -> {
        int value = mode.read1(bus, state);
        state.flagZ = (value & state.regA) == 0;
        state.flagV = (value & (1 << 6)) != 0;
        state.flagN = (value & (1 << 7)) != 0;
    }),
    DEX((bus, state, mode) -> state.regX = setNZFlags(state, (state.regX - 1) & 0xFF)),
    DEC((bus, state, mode) -> mode.write1(bus, state, setNZFlags(state, (mode.read1(bus, state) - 1) & 0xFF))),
    TXA((bus, state, mode) -> state.regA = setNZFlags(state, state.regX)),
    TXS(((bus, state, mode) -> state.regSp = state.regX)),
    LSR((bus, state, mode) -> {
        int old = mode.read1(bus, state);
        state.flagC = (old & 1) == 1;
        mode.write1(bus, state, setNZFlags(state, old >> 1));
    }),
    ASL((bus, state, mode) -> {
        int old = mode.read1(bus, state);
        int result = (old << 1) & 0xFF;
        
        state.flagC = (old & (1 << 7)) != 0;
        state.flagZ = state.regA == 0; //TODO: Check this, it seems a bit odd
        state.flagN = (result & (1 << 7)) != 0;
        
        mode.write1(bus, state, result);
    }),
    PHA((bus, state, mode) -> MemoryUtils.stackPush1(state.regA, bus, state)),
    PLA((bus, state, mode) -> state.regA = MemoryUtils.setNZFlags(state, MemoryUtils.stackPop1(bus, state))),
    JMP((bus, state, mode) -> state.regPc = state.regMar),
    NOP((bus, state, mode) -> {});
    
    private final InstructionProcessor processor;
    
    Instruction(InstructionProcessor processor) {
        this.processor = processor;
    }
    
    public InstructionProcessor getProcessor() {
        return processor;
    }
    
}
