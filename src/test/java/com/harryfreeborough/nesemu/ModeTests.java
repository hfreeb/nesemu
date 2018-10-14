package com.harryfreeborough.nesemu;

import static org.junit.Assert.assertEquals;

import com.harryfreeborough.nesemu.devices.Memory;
import com.harryfreeborough.nesemu.utils.MemoryUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ModeTests {

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
        assertEquals(1, this.store.regA);

        cpu.tick();
        assertEquals(3, this.store.regA);

        cpu.tick();
        assertEquals(6, this.store.regA);

        cpu.tick();
        assertEquals(10, this.store.regA);

        cpu.tick();
        assertEquals(15, this.store.regA);

        cpu.tick();
        assertEquals(21, this.store.regA);

        cpu.tick();
        assertEquals(28, this.store.regA);

        cpu.tick();
        assertEquals(36, this.store.regA);
    }

    @Test
    public void test() {
        /*
        ADC #$1
        ADC $0xF0
        ADC $0xF0,X
        ADC $0x0200
        ADC $0x0200,X
        ADC $0x0200,Y
        ADC ($0x90,X)
        ADC ($0x90),Y
         */

        this.store.regX = 2;
        this.store.regY = 4;

        this.bus.write1(0x00F0, 2);
        this.bus.write1(0x00F2, 3);
        this.bus.write1(0x0200, 4);
        this.bus.write1(0x0202, 5);
        this.bus.write1(0x0204, 6);
        this.bus.write1(0x0300, 7);
        this.bus.write1(0x0304, 8);
        this.bus.write2(0x0092, 0x0300);
        this.bus.write2(0x0090, 0x0300);

        MemoryUtils.programWrite(this.bus, this.store,
                0x69, 1,
                0x65, 0xF0,
                0x75, 0xF0,
                0x6D, 0x00, 0x02,
                0x7D, 0x00, 0x02,
                0x79, 0x00, 0x02,
                0x61, 0x90,
                0x71, 0x90);
    }

}
