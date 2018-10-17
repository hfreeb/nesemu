package com.harryfreeborough.nesemu.instruction;

public enum Command {

    ADC_IM(0x69, Instruction.ADC, AddressingMode.IMMEDIATE),
    LDA_ZP(0xA5, Instruction.LDA, AddressingMode.ZERO_PAGE),
    CMP_IM(0xC9, Instruction.CMP, AddressingMode.IMMEDIATE),
    BNE_RE(0xD0, Instruction.BNE, AddressingMode.RELATIVE);

    private final int opcode;
    private final Instruction instruction;
    private final AddressingMode addressingMode;

    Command(int opcode, Instruction instruction, AddressingMode addressingMode) {
        this.opcode = opcode;
        this.instruction = instruction;
        this.addressingMode = addressingMode;
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

}
