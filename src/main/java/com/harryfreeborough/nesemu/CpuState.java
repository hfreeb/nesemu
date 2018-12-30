package com.harryfreeborough.nesemu;

public class CpuState {
    
    //CPU Cycles that need to be catched up on by other devices
    //(i.e. PPU, APU and controller processor, WE it is called /TODO)
    public int cycles;
    
    //16-bit program counter
    public int regPc;
    
    //8-bit stack pointer
    public int regSp;
    
    //8-bit accumulator register
    public int regA;
    
    //8-bit index registers
    public int regX;
    public int regY;
    
    //16-bit memory address register for internal use to make things easier
    //TODO: Kind of hacky, possibly change design of AddressingMode to avoid this
    //(previously calling read1 and write1 in one instruction would not work for
    // modes which pop from program memory)
    public int regMar;
    
    //Carry flag - Enables multi-byte arithmetic operations and indicates whether
    //the result of the previous operation on the accumulator register
    //overflowed from bit 7 or underflowed from bit 0.
    public boolean flagC;
    
    //Zero flag - Indicates whether the result of the previous operation
    //on the accumulator register was 0.
    public boolean flagZ;
    
    //Interrupt flag - Indicates whether interrupts are either prevented (false)
    //or enabled (true).
    public boolean flagI;
    
    //Decimal flag - Indicates whether BCD (binary coded decimal) mode is used
    //in arithmetic operations (true) or binary mode is used (false).
    public boolean flagD;
    
    //Break flag - Indicates whether a BRK instruction has been executed
    //and is being handled.
    public boolean flagB;
    
    //Unused flag
    public boolean flagU;
    
    //Overflow flag - Indicates whether the result of the previous operation
    //on the accumulator register was too large to fit in the register width
    //(less than -128 or more than 127 in the two's complement representation).
    public boolean flagV;
    
    public boolean flagN;
    
}
