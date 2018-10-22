package com.harryfreeborough.nesemu.utils;

import com.harryfreeborough.nesemu.CpuState;
import com.harryfreeborough.nesemu.device.MemoryBus;

public class MemoryUtils {
    
    private MemoryUtils() {
    }
    
    public static int programPop1(MemoryBus bus, CpuState state) {
        int value = bus.read1(state.regPc);
        state.regPc = (state.regPc + 1) & 0xFFFF;
        return value;
    }
    
    public static int programPop2(MemoryBus bus, CpuState state) {
        int value = bus.read2(state.regPc);
        state.regPc = (state.regPc + 2) & 0xFFFF;
        return value;
    }
    
    public static void programWrite(MemoryBus bus, CpuState state, int... data) {
        for (int i = 0; i < data.length; i++) {
            bus.write1(state.regPc + i, data[i]);
        }
    }
    
    public static void stackPush1(int value, MemoryBus bus, CpuState state) {
        bus.write1(state.regSp + 0x100, value);
        state.regSp = (state.regSp - 1) & 0xFF;
    }
    
    public static void stackPush2(int value, MemoryBus bus, CpuState state) {
        stackPush1(value & 0xFF, bus, state);
        stackPush1(value >> 8, bus, state);
    }
    
    public static int stackPop1(MemoryBus bus, CpuState state) {
        state.regSp = (state.regSp + 1) & 0xFF;
        return bus.read1(state.regSp + 0x100);
    }
    
    public static int stackPop2(MemoryBus bus, CpuState state) {
        return (stackPop1(bus, state) << 8) | (stackPop1(bus, state) & 0xFF);
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
