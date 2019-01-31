package com.harryfreeborough.nesemu.utils;

import com.harryfreeborough.nesemu.cpu.CpuMemory;
import com.harryfreeborough.nesemu.cpu.CpuState;

public class MemoryUtils {
    
    private MemoryUtils() {
    }
    
    public static int shiftBit(boolean flag, int shift) {
        return (flag ? 1 : 0) << shift;
    }
    
    public static boolean bitPresent(int value, int bit) {
        return ((value >> bit) & 0x01) == 0x01;
    }
    
    public static int programPop1(CpuMemory bus, CpuState state) {
        int value = bus.read1(state.regPc);
        state.regPc = (state.regPc + 1) & 0xFFFF;
        return value;
    }
    
    public static int programPop2(CpuMemory bus, CpuState state) {
        int value = bus.read2(state.regPc);
        state.regPc = (state.regPc + 2) & 0xFFFF;
        return value;
    }
    
    public static void programWrite(CpuMemory bus, CpuState state, int... data) {
        for (int i = 0; i < data.length; i++) {
            bus.write1(state.regPc + i, data[i]);
        }
    }
    
    public static void stackPush1(int value, CpuMemory bus, CpuState state) {
        bus.write1(0x100 | state.regSp, value);
        state.regSp = (state.regSp - 1) & 0xFF;
    }
    
    public static void stackPush2(int value, CpuMemory bus, CpuState state) {
        stackPush1(value >> 8, bus, state);
        stackPush1(value & 0xFF, bus, state);
    }
    
    public static int stackPop1(CpuMemory bus, CpuState state) {
        state.regSp = (state.regSp + 1) & 0xFF;
        return bus.read1(0x100 | state.regSp);
    }
    
    public static int stackPop2(CpuMemory bus, CpuState state) {
        int lsb = stackPop1(bus, state);
        int msb = stackPop1(bus, state);
        return lsb | (msb << 8);
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
