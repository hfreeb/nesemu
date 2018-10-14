package com.harryfreeborough.nesemu;

import com.harryfreeborough.nesemu.devices.Memory;
import com.harryfreeborough.nesemu.utils.MemoryUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BitwiseTests {

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
        cpu.tick();

        /*cpu.tick();
        assertEquals(0b11010011, this.store.regA);

        cpu.tick();
        assertEquals(0b10100110, this.store.regA);*/
    }

    @Test
    public void test() {
        /*
        AND #0b11011011
        ASL
         */

        this.store.regA = 0b11110111;
        MemoryUtils.programWrite(this.bus, this.store, 0x29, 0b11011011, 0x0A);
    }

}
