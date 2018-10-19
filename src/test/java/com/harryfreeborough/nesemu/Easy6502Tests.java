package com.harryfreeborough.nesemu;

import com.harryfreeborough.nesemu.devices.Memory;
import com.harryfreeborough.nesemu.utils.MemoryUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Easy6502Tests {

    private Cpu cpu;
    private MemoryBus bus;
    private CpuState store;

    @Before
    public void setup() {
        this.bus = new MemoryBus();
        this.store = new CpuState();
        this.cpu = new Cpu(this.bus, this.store);

        this.bus.addDevice(new Memory(0x0000, 0x0FFF, false));
    }

    @Test
    public void loopTest() {
        /*
        LDX #0
        LDY #0
        w: INY
        v: INX
        CPX #255
        BNE v
        CPY #255
        BNE w
         */

        /*
        MemoryUtils.programWrite(this.bus, this.store,
                0xA2, 0,
                0xA0, 0,
                0xC8,
                0xE8,
                0xE0, 0xFF,
                0xD0, 0xFB,
                0xC0, 0xFF,
                0xD0, 0xF6);

        long start = System.currentTimeMillis();
        while (this.cpu.run()) ;
        long end = System.currentTimeMillis();
        assertEquals(0xFF, this.store.regX);
        assertEquals(0xFF, this.store.regY);

        System.out.println("65k iterations took " + (end - start) + " ms");*/
    }

    @Test
    public void ourFirstProgram() {
        /*
        LDA #$01
        STA $0200
        LDA #$05
        STA $0201
        LDA #$08
        STA $0202
         */
        MemoryUtils.programWrite(this.bus, this.store,
                0xA9, 0x01,
                0x8D, 0x00, 0x02,
                0xA9, 0x05,
                0x8D, 0x01, 0x02,
                0xA9, 0x08,
                0x8D, 0x02, 0x02);

        while (this.cpu.run()) ;
        assertEquals(this.cpu.getBus().read1(0x0200), 1);
        assertEquals(this.cpu.getBus().read1(0x0201), 5);
        assertEquals(this.cpu.getBus().read1(0x0202), 8);
    }

}
