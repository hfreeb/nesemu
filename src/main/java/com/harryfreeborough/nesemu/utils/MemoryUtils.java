package com.harryfreeborough.nesemu.utils;

import com.harryfreeborough.nesemu.cpu.CpuMemory;
import com.harryfreeborough.nesemu.cpu.CpuState;

/**
 * Provides utilities for easily reading from and writing to memory.
 */
public class MemoryUtils {

    private MemoryUtils() {
    }

    public static int shiftBit(boolean flag, int shift) {
        return (flag ? 1 : 0) << shift;
    }

    public static boolean bitSet(int value, int bit) {
        return ((value >> bit) & 0x01) == 0x01;
    }

    public static int programPop1(CpuMemory memory, CpuState state) {
        int value = memory.read1(state.regPc);
        state.regPc = (state.regPc + 1) & 0xFFFF;
        return value;
    }

    public static int programPop2(CpuMemory memory, CpuState state) {
        int value = memory.read2(state.regPc);
        state.regPc = (state.regPc + 2) & 0xFFFF;
        return value;
    }

    public static void programWrite(CpuMemory memory, CpuState state, int... data) {
        for (int i = 0; i < data.length; i++) {
            memory.write1(state.regPc + i, data[i]);
        }
    }

    public static int signedByteToInt(int b) {
        //TODO: Better logic?
        int masked = b & 0b01111111;
        if ((b & 0b10000000) != 0) {
            masked = -128 + masked;
        }
        return masked;
    }

    public static int setNZFlags(CpuState state, int value) {
        state.flagZ = value == 0;
        state.flagN = (value & 1 << 7) != 0;
        return value;
    }

}
