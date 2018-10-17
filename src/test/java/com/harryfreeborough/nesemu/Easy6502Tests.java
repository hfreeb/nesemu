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
        LDA $00 (value at 00)
        l: ADC #1
        CMP #10
        BNE l
         */
        
        MemoryUtils.programWrite(this.bus, this.store,
                0xA5, 0,
                0x69, 1,
                0xC9, 10,
                0xD0, 0xFA);
        
        while (this.cpu.run()) ;
        assertEquals(10, this.store.regA);
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
        assertEquals(10, this.store.regA);
    }
    
}
