package com.harryfreeborough.nesemu.cpu;

import java.util.LinkedList;
import java.util.Queue;

import static com.harryfreeborough.nesemu.utils.MemoryUtils.bitPresent;
import static com.harryfreeborough.nesemu.utils.MemoryUtils.shiftBit;

public class CpuState {
    
    public final byte[] internalRam = new byte[0x800];

    public final boolean[] buttonState = new boolean[8];
    public final Queue<Boolean> buttonStateCache = new LinkedList<>();

    public boolean flagStrobe;

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
    
    public int getStatus() {
        int status = 0;
        status |= shiftBit(this.flagC, 0);
        status |= shiftBit(this.flagZ, 1);
        status |= shiftBit(this.flagI, 2);
        status |= shiftBit(this.flagD, 3);
        status |= shiftBit(this.flagB, 4);
        status |= shiftBit(this.flagU, 5);
        status |= shiftBit(this.flagV, 6);
        status |= shiftBit(this.flagN, 7);
        return status;
    }
    
    public void setStatus(int value) {
        this.flagC = bitPresent(value, 0);
        this.flagZ = bitPresent(value, 1);
        this.flagI = bitPresent(value, 2);
        this.flagD = bitPresent(value, 3);
        this.flagB = bitPresent(value, 4);
        this.flagU = bitPresent(value, 5);
        this.flagV = bitPresent(value, 6);
        this.flagN = bitPresent(value, 7);
    }
    
}
