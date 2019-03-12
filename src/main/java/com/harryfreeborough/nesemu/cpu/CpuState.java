package com.harryfreeborough.nesemu.cpu;

import static com.harryfreeborough.nesemu.utils.MemoryUtils.bitSet;
import static com.harryfreeborough.nesemu.utils.MemoryUtils.shiftBit;

/**
 * Holds the state that the {@link Cpu} has.
 */
public class CpuState {

    public final byte[] internalRam = new byte[0x800];

    //Controller strobing
    public boolean flagStrobe;

    //Cycles elapsed this session
    public int cycles;

    //16-bit program counter
    public int regPc;

    //8-bit stack pointer
    public int regSp = 0xFD;

    //8-bit accumulator register
    public int regA;

    //8-bit index registers
    public int regX;
    public int regY;

    //16-bit memory address register for internal use
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
    public boolean flagI = true;

    //Decimal flag - Indicates whether BCD (binary coded decimal) mode is used
    //in arithmetic operations (true) or binary mode is used (false).
    public boolean flagD;

    //Overflow flag - Indicates whether the result of the previous operation
    //on the accumulator register was too large to fit in the register width
    //(less than -128 or more than 127 in the two's complement representation).
    public boolean flagV;

    //Negative flag - Set when an arithmetic operation results in a negative value.
    public boolean flagN;

    public void initPc(CpuMemory memory) {
        this.regPc = memory.read2(0xFFFC);
    }

    public int getStatus() {
        int status = 0;
        status |= shiftBit(this.flagC, 0);
        status |= shiftBit(this.flagZ, 1);
        status |= shiftBit(this.flagI, 2);
        status |= shiftBit(this.flagD, 3);
        //Unused flag, always set
        status |= 1 << 4;
        //Break flag, always set except when pushing processor status when jumping to an interrupt routine for a hardware interrupt.
        status |= 1 << 5;
        status |= shiftBit(this.flagV, 6);
        status |= shiftBit(this.flagN, 7);
        return status;
    }

    public void setStatus(int value) {
        this.flagC = bitSet(value, 0);
        this.flagZ = bitSet(value, 1);
        this.flagI = bitSet(value, 2);
        this.flagD = bitSet(value, 3);
        this.flagV = bitSet(value, 6);
        this.flagN = bitSet(value, 7);
    }

    public void copy(CpuState state) {
        this.flagC = state.flagC;
        this.flagZ = state.flagZ;
        this.flagI = state.flagI;
        this.flagD = state.flagD;
        this.flagV = state.flagV;
        this.flagN = state.flagN;

        this.cycles = state.cycles;
        this.regPc = state.regPc;
        this.regSp = state.regSp;
        this.regA = state.regA;
        this.regX = state.regX;
        this.regY = state.regY;

        System.arraycopy(state.internalRam, 0, this.internalRam, 0, state.internalRam.length);
    }

    public void reset() {
        copy(new CpuState());
    }

    @Override
    public CpuState clone() {
        CpuState state = new CpuState();
        state.copy(this);
        return state;
    }

}
