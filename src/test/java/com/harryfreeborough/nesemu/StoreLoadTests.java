package com.harryfreeborough.nesemu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.harryfreeborough.nesemu.devices.Memory;
import com.harryfreeborough.nesemu.utils.MemoryUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StoreLoadTests {

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
        assertEquals(15, this.store.regA);

        cpu.tick();
        assertEquals(15, this.bus.read1(0x0000));

        cpu.tick();
        assertEquals(15, this.store.regX);

        cpu.tick();
        assertEquals(15, this.store.regY);

        cpu.tick();
        assertEquals(15, this.store.regS);

        cpu.tick();
        assertFalse(this.store.flagD);
    }

    @Test
    public void test() {
        /*
        LDA #$15
        STA $0
        LDX #$15
        LDY #$15
        TXS
        CLD
         */

        this.store.flagD = true;

        MemoryUtils.programWrite(this.bus, this.store,
                0xA9, 15,
                0x8D, 0, 0,
                0xA2, 15,
                0xA0, 15,
                0x9A,
                0xD8);
    }

}
