package com.harryfreeborough.nesemu.instruction;

import static com.harryfreeborough.nesemu.instruction.AddressingMode.*;
import static com.harryfreeborough.nesemu.instruction.Instruction.*;

public enum Command {

    ADC_IMM(0x69, ADC, IMM, 2),
    ADC_ZPG(0x65, ADC, ZPG, 3),
    ADC_ZPX(0x75, ADC, ZPX, 4),
    ADC_ABS(0x6D, ADC, ABS, 4),
    LDA_ZPG(0xA5, LDA, ZPG),
    LDA_IMM(0xA9, LDA, IMM),
    CMP_IMM(0xC9, CMP, IMM),
    BNE_REL(0xD0, BNE, REL),
    STA_ABS(0x8D, STA, ABS),
    LDX_IMM(0xA2, LDX, IMM),
    LDY_IMM(0xA0, LDY, IMM),
    INY_IMP(0xC8, INY, IMP),
    INX_IMP(0xE8, INX, IMP),
    CPX_IMM(0xE0, CPX, IMM),
    CPY_IMM(0xC0, CPY, IMM);

    private final int opcode;
    private final Instruction instruction;
    private final AddressingMode addressingMode;
    private final int cycles;

    Command(int opcode, Instruction instruction, AddressingMode addressingMode, int cycles) {
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
