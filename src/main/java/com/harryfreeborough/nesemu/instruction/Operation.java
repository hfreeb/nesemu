package com.harryfreeborough.nesemu.instruction;

import static com.harryfreeborough.nesemu.instruction.AddressingMode.*;
import static com.harryfreeborough.nesemu.instruction.Instruction.*;

/**
 * Holds all MOS 6502 opcode to instruction and addressing mode mappings.
 */
public enum Operation {

    ADC_IMM(0x69, ADC, IMM, 2),
    ADC_ZPG(0x65, ADC, ZPG, 3),
    ADC_ZPX(0x75, ADC, ZPX, 4),
    ADC_ABS(0x6D, ADC, ABS, 4),
    ADC_ABX(0x7D, ADC, ABX, 4),
    ADC_ABY(0x79, ADC, ABY, 4),
    ADC_IDX(0x61, ADC, IDX, 6),
    ADC_IDY(0x71, ADC, IDY, 5),

    SBC_IMM(0xE9, SBC, IMM, 2),
    SBC_ZPG(0xE5, SBC, ZPG, 3),
    SBC_ZPX(0xF5, SBC, ZPX, 4),
    SBC_ABS(0xED, SBC, ABS, 4),
    SBC_ABX(0xFD, SBC, ABX, 4),
    SBC_ABY(0xF9, SBC, ABY, 4),
    SBC_IDX(0xE1, SBC, IDX, 6),
    SBC_IDY(0xF1, SBC, IDY, 5),

    AND_IMM(0x29, AND, IMM, 2),
    AND_ZPG(0x25, AND, ZPG, 3),
    AND_ZPX(0x35, AND, ZPX, 4),
    AND_ABS(0x2D, AND, ABS, 4),
    AND_ABX(0x3D, AND, ABX, 4),
    AND_ABY(0x39, AND, ABY, 4),
    AND_IDX(0x21, AND, IDX, 6),
    AND_IDY(0x31, AND, IDY, 5),

    ORA_IMM(0x09, ORA, IMM, 2),
    ORA_ZPG(0x05, ORA, ZPG, 3),
    ORA_ZPX(0x15, ORA, ZPX, 4),
    ORA_ABS(0x0D, ORA, ABS, 4),
    ORA_ABX(0x1D, ORA, ABX, 4),
    ORA_ABY(0x19, ORA, ABY, 4),
    ORA_IDX(0x01, ORA, IDX, 6),
    ORA_IDY(0x11, ORA, IDY, 5),

    EOR_IMM(0x49, EOR, IMM, 2),
    EOR_ZPG(0x45, EOR, ZPG, 3),
    EOR_ZPX(0x55, EOR, ZPX, 4),
    EOR_ABS(0x4D, EOR, ABS, 4),
    EOR_ABX(0x5D, EOR, ABX, 4),
    EOR_ABY(0x59, EOR, ABY, 4),
    EOR_IDX(0x41, EOR, IDX, 6),
    EOR_IDY(0x51, EOR, IDY, 5),

    ROL_ACC(0x2A, ROL, ACC, 2),
    ROL_ZPG(0x26, ROL, ZPG, 5),
    ROL_ZPX(0x36, ROL, ZPX, 6),
    ROL_ABS(0x2E, ROL, ABS, 6),
    ROL_ABX(0x3E, ROL, ABX, 7),

    ROR_ACC(0x6A, ROR, ACC, 2),
    ROR_ZPG(0x66, ROR, ZPG, 5),
    ROR_ZPX(0x76, ROR, ZPX, 6),
    ROR_ABS(0x6E, ROR, ABS, 6),
    ROR_ABX(0x7E, ROR, ABX, 7),

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
    BMI_REL(0x30, BMI, REL, 2),
    BPL_REL(0x10, BPL, REL, 2),
    BCS_REL(0xB0, BCS, REL, 2),
    BCC_REL(0x90, BCC, REL, 2),
    BVS_REL(0x70, BVS, REL, 2),
    BVC_REL(0x50, BVC, REL, 2),

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
    DEY_IMP(0x88, DEY, IMP, 2),
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

    STX_ZPG(0x86, STX, ZPG, 3),
    STX_ZPY(0x96, STX, ZPY, 4),
    STX_ABS(0x8E, STX, ABS, 4),

    STY_ZPG(0x84, STY, ZPG, 3),
    STY_ZPX(0x94, STY, ZPX, 4),
    STY_ABS(0x8C, STY, ABS, 4),

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

    PHA_IMP(0x48, PHA, IMP, 3),
    PLP_IMP(0x28, PLP, IMP, 4),
    PHP_IMP(0x08, PHP, IMP, 3),
    PLA_IMP(0x68, PLA, IMP, 4),

    JMP_ABS(0x4C, JMP, ABS, 3),
    JMP_IND(0x6C, JMP, IND, 5),

    CLC_IMP(0x18, CLC, IMP, 2),
    SEC_IMP(0x38, SEC, IMP, 2),
    CLI_IMP(0x58, CLI, IMP, 2),
    SEI_IMP(0x78, SEI, IMP, 2),
    CLV_IMP(0xB8, CLV, IMP, 2),
    SED_IMP(0xF8, SED, IMP, 2),
    CLD_IMP(0xD8, CLD, IMP, 2),

    JSR_ABS(0x20, JSR, ABS, 6),
    RTS_IMP(0x60, RTS, IMP, 6),
    BIT_ZPG(0x24, BIT, ZPG, 3),
    BIT_ABS(0x2C, BIT, ABS, 4),
    TAX_IMP(0xAA, TAX, IMP, 2),
    TAY_IMP(0xA8, TAY, IMP, 2),
    TXA_IMP(0x8A, TXA, IMP, 2),
    TYA_IMP(0x98, TYA, IMP, 2),
    TXS_IMP(0x9A, TXS, IMP, 2),
    TSX_IMP(0xBA, TSX, IMP, 2),

    BRK_IMP(0x00, BRK, IMP, 7),
    RTI_IMP(0x40, RTI, IMP, 6),
    NOP_IMP(0xEA, NOP, IMP, 2),

    /* Unofficial opcodes */
    RLA_ZPG(0x27, RLA, ZPG, 5),
    RLA_ZPX(0x37, RLA, ZPX, 6),
    RLA_ABS(0x2F, RLA, ABS, 6),
    RLA_ABX(0x3F, RLA, ABX, 7),
    RLA_ABY(0x3B, RLA, ABY, 7),
    RLA_IDX(0x23, RLA, IDX, 8),
    RLA_IDY(0x33, RLA, IDY, 8);

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
