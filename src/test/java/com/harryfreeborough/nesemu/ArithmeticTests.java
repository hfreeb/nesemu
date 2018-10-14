package com.harryfreeborough.nesemu;

import static org.junit.Assert.assertEquals;

import com.harryfreeborough.nesemu.devices.Memory;
import com.harryfreeborough.nesemu.utils.MemoryUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ArithmeticTests {

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

    @After
    public void verify() {
        this.cpu.execute();

        assertEquals(15, this.store.regA);
    }

    @Test
    public void adc() {
        /*
        ADC #$15
         */

        MemoryUtils.programWrite(this.bus, this.store, 0x69, 15);
    }

}
