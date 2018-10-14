package com.harryfreeborough.nesemu;

import static org.junit.Assert.assertEquals;

import com.harryfreeborough.nesemu.devices.Memory;
import com.harryfreeborough.nesemu.utils.MemoryUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class TimeTest {

    private Emu6502 emulator;
    private MemoryBus bus;
    private CpuState store;

    @Before
    public void setup() {
        this.emulator = new Emu6502();
        this.bus = this.emulator.getBus();
        this.store = this.emulator.getStore();

        this.bus.addDevice(new Memory(0x0000, 0x0FFF, false));
    }

    @After
    public void verify() {
        Cpu cpu = this.emulator.getCpu();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 2500; i++) {
            cpu.tick();
        }
        System.out.println("Test took " + (System.currentTimeMillis() - startTime) + "ms");
    }

    @Test
    public void test() {
        int[] instructions = new int[2500];

        for (int i = 0; i < instructions.length; i++) {
            instructions[i] = 0xC8;
        }

        MemoryUtils.programWrite(this.bus, this.store, instructions);
    }

}
