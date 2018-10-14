package com.harryfreeborough.nesemu;

import static org.junit.Assert.assertEquals;

import com.harryfreeborough.nesemu.devices.Memory;
import com.harryfreeborough.nesemu.utils.MemoryUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JmpTests {

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
        cpu.tick();
        cpu.tick();
        cpu.tick();
        cpu.tick();
        cpu.tick();

        assertEquals(15, store.regA);
    }

    @Test
    public void subroutineTests() {
        /*
        JSR teleport
        ADC #$8
        JMP betterTeleport
        teleport: ADC #$4
        RTS
        betterTeleport: ADC #$3
         */

        int teleport = this.store.regPc + 8;
        int betterTeleport = this.store.regPc + 11;
        MemoryUtils.programWrite(this.bus, this.store,
                0x20, teleport & 0xFF, teleport >> 8, //read correctly
                0x69, 8,
                0x4C, betterTeleport & 0xFF, betterTeleport >> 8, //Here it is reading 0x0469
                0x69, 4,
                0x60,
                0x69, 3);
    }

}
