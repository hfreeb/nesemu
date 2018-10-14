package com.harryfreeborough.nesemu;

public class CpuState {

    //8-bit accumulator register
    public int regA = 0;

    //8-bit index registers
    public int regX = 0;
    public int regY = 0;

    //8-bit stack pointer
    public int regS = 0xFF;

    //16-bit program counter
    public int regPc = 0x400;

    public boolean flagN = false;

    //Overflow flag - Indicates whether the result of the previous operation
    //on the accumulator register was too large to fit in the register width
    //(less than -128 or more than 127 in the two's complement representation).
    public boolean flagV = false;

    //Break flag - Indicates whether a BRK instruction has been executed
    //and is being handled.
    public boolean flagB = false;

    //Decimal flag - Indicates whether BCD (binary coded decimal) mode is used
    //in arithmetic operations (true) or binary mode is used (false).
    public boolean flagD = false;

    //Interrupt flag - Indicates whether interrupts are either prevented (false)
    //or enabled (true).
    public boolean flagI = true;

    //Zero flag - Indicates whether the result of the previous operation
    //on the accumulator register was 0.
    public boolean flagZ = false;

    //Carry flag - Enables multi-byte arithmetic operations and indicates whether
    //the result of the previous operation on the accumulator register
    //overflowed from bit 7 or underflowed from bit 0.
    public boolean flagC = false;

}
