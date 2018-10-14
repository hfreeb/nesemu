package com.harryfreeborough.nesemu.utils;

import com.harryfreeborough.nesemu.MemoryBus;
import com.harryfreeborough.nesemu.CpuState;

public class MemoryUtils {

    private MemoryUtils() {
    }

    public static int programPop1(MemoryBus bus, CpuState store) {
        int value = bus.read1(store.regPc);
        store.regPc = (store.regPc + 1) & 0xFFFF;
        return value;
    }

    public static int programPop2(MemoryBus bus, CpuState store) {
        int value = bus.read2(store.regPc);
        store.regPc = (store.regPc + 2) & 0xFFFF;
        return value;
    }

    public static void programWrite(MemoryBus bus, CpuState store, int... data) {
        for (int i = 0; i < data.length; i++) {
            bus.write1(store.regPc + i, data[i]);
        }
    }

    public static void stackPush1(int value, MemoryBus bus, CpuState store) {
        bus.write1(store.regS + 0x100, value);
        store.regS = (store.regS - 1) & 0xFF;
    }

    public static void stackPush2(int value, MemoryBus bus, CpuState store) {
        stackPush1(value & 0xFF, bus, store);
        stackPush1(value >> 8, bus, store);
    }

    public static int stackPop1(MemoryBus bus, CpuState store) {
        store.regS = (store.regS + 1) & 0xFF;
        return bus.read1(store.regS + 0x100);
    }

    public static int stackPop2(MemoryBus bus, CpuState store) {
        return (stackPop1(bus, store) << 8) | (stackPop1(bus, store) & 0xFF);
    }

}
