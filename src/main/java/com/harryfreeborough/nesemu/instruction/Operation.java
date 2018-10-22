package com.harryfreeborough.nesemu.instruction;

import static com.harryfreeborough.nesemu.instruction.AddressingMode.*;
import static com.harryfreeborough.nesemu.instruction.Instruction.*;

public enum Operation {
    
    ADC_IMM(0x69, ADC, IMM, 2),
    ADC_ZPG(0x65, ADC, ZPG, 3),
    ADC_ZPX(0x75, ADC, ZPX, 4),
    ADC_ABS(0x6D, ADC, ABS, 4),
    ADC_ABX(0x7D, ADC, ABX, 4),
    ADC_ABY(0x79, ADC, ABY, 4),
    ADC_IDX(0x61, ADC, IDX, 6),
    ADC_IDY(0x71, ADC, IDY, 5),
    
    AND_IMM(0x29, AND, IMM, 2),
    AND_ZPG(0x25, AND, ZPG, 3),
    AND_ZPX(0x35, AND, ZPX, 4),
    AND_ABS(0x2D, AND, ABS, 4),
    AND_ABX(0x3D, AND, ABX, 4),
    AND_ABY(0x39, AND, ABY, 4),
    AND_IDX(0x21, AND, IDX, 6),
    AND_IDY(0x31, AND, IDY, 5),
    
    LDA_IMM(0xA9, LDA, IMM, 2),
    LDA_ZPG(0xA5, LDA, ZPG, 3),
    LDA_ZPX(0xB5, LDA, ZPX, 4),
    LDA_ABS(0xAD, LDA, ABS, 4),
    LDA_ABX(0xBD, LDA, ABX, 4),
    LDA_ABY(0xB9, LDA, ABY, 4),
    LDA_IDX(0xA1, LDA, IDX, 6),
    LDA_IDY(0xB1, LDA, IDY, 5),
    
    BEQ_REL(0xF0, BEQ, REL, 2),
    BNE_REL(0xD0, BNE, REL, 2),
    BPL_REL(0x10, BPL, REL, 2),
    BCS_REL(0xB0, BCS, REL, 2),
    BCC_REL(0x90, BCC, REL, 2),
    
    LSR_ACC(0x4A, LSR, ACC, 2),
    LSR_ZPG(0x46, LSR, ZPG, 5),
    LSR_ZPX(0x56, LSR, ZPX, 6),
    LSR_ABS(0x4E, LSR, ABS, 6),
    LSR_ABX(0x5E, LSR, ABX, 7),
    
    ASL_ACC(0x0A, ASL, ACC, 2),
    ASL_ZPG(0x06, ASL, ZPG, 5),
    ASL_ZPX(0x16, ASL, ZPX, 6),
    ASL_ABS(0x0E, ASL, ABS, 6),
    ASL_ABX(0x1E, ASL, ABX, 7),
    
    DEX_IMP(0xCA, DEX, IMP, 2),
    DEC_ZPG(0xC6, DEC, ZPG, 5),
    DEC_ZPX(0xD6, DEC, ZPX, 6),
    DEC_ABS(0xCE, DEC, ABS, 6),
    DEC_ABX(0xDE, DEC, ABX, 7),
    
    CMP_IMM(0xC9, CMP, IMM, 2),
    CMP_ZPG(0xC5, CMP, ZPG, 3),
    CMP_ZPX(0xD5, CMP, ZPX, 4),
    CMP_ABS(0xCD, CMP, ABS, 4),
    CMP_ABX(0xDD, CMP, ABX, 4),
    CMP_ABY(0xD9, CMP, ABY, 4),
    CMP_IDX(0xC1, CMP, IDX, 6),
    CMP_IDY(0xD1, CMP, IDY, 5),
    
    CPX_IMM(0xE0, CPX, IMM, 2),
    CPX_ZPG(0xE4, CPX, ZPG, 3),
    CPX_ABS(0xEC, CPX, ABS, 4),
    CPY_IMM(0xC0, CPY, IMM, 2),
    CPY_ZPG(0xC4, CPY, ZPG, 3),
    CPY_ABS(0xCC, CPY, ABS, 4),
    
    STA_ZPG(0x85, STA, ZPG, 3),
    STA_ZPX(0x95, STA, ZPX, 4),
    STA_ABS(0x8D, STA, ABS, 4),
    STA_ABX(0x9D, STA, ABX, 5),
    STA_ABY(0x99, STA, ABY, 5),
    STA_IDX(0x81, STA, IDX, 6),
    STA_IDY(0x91, STA, IDY, 6),
    
    LDX_IMM(0xA2, LDX, IMM, 2),
    LDX_ZPG(0xA6, LDX, ZPG, 3),
    LDX_ZPY(0xB6, LDX, ZPY, 4),
    LDX_ABS(0xAE, LDX, ABS, 4),
    LDX_ABY(0xBE, LDX, ABY, 4),
    
    LDY_IMM(0xA0, LDY, IMM, 2),
    LDY_ZPG(0xA4, LDY, ZPG, 3),
    LDY_ZPX(0xB4, LDY, ZPX, 4),
    LDY_ABS(0xAC, LDY, ABS, 4),
    LDY_ABX(0xBC, LDY, ABX, 4),
    
    INY_IMP(0xC8, INY, IMP, 2),
    INX_IMP(0xE8, INX, IMP, 2),
    INC_ZPG(0xE6, INC, ZPG, 5),
    INC_ZPX(0xF6, INC, ZPX, 6),
    INC_ABS(0xEE, INC, ABS, 6),
    INC_ABX(0xFE, INC, ABX, 7),
    
    JSR_ABS(0x20, JSR, ABS, 6),
    RTS_IMP(0x60, RTS, IMP, 6),
    CLC_IMP(0x18, CLC, IMP, 2),
    BIT_ZPG(0x24, BIT, ZPG, 3),
    BIT_ABS(0x2C, BIT, ABS, 4),
    TXA_IMP(0x8A, TXA, IMP, 2),
    NOP_IMP(0xEA, NOP, IMP, 2);
    
    private final int opcode;
    private final Instruction instruction;
    private final AddressingMode addressingMode;
    private final int cycles;
    
    Operation(int opcode, Instruction instruction, AddressingMode addressingMode, int cycles) {
        this.opcode = opcode;
        this.instruction = instruction;
        this.addressingMode = addressingMode;
        this.cycles = cycles;
    }
    
    public int getOpcode() {
        return this.opcode;
    }
    
    public Instruction getInstruction() {
        return this.instruction;
    }
    
    public AddressingMode getAddressingMode() {
        return this.addressingMode;
    }
    
    public int getCycles() {
        return this.cycles;
    }
    
}
