package com.harryfreeborough.nesemu;

import com.harryfreeborough.nesemu.device.MemoryBus;
import com.harryfreeborough.nesemu.instruction.Operation;
import com.harryfreeborough.nesemu.utils.MemoryUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MysteryTests {
    
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
        AND #$FE
        STA $0201
        LDA #$08
        STA $0202
         */
        Console console = new Console();
        MemoryBus bus = console.getCpu().getBus();
        
        MemoryUtils.programWrite(bus, console.getCpu().getState(),
                0xA9, 0x01,
                0x8D, 0x00, 0x02,
                0xA9, 0x05,
                0x29, 0xFE,
                0x8D, 0x01, 0x02,
                0xA9, 0x08,
                0x8D, 0x02, 0x02);
        
        while (console.getCpu().tick()) ;
        assertEquals(1, bus.read1(0x0200));
        assertEquals(4, bus.read1(0x0201));
        assertEquals(8, bus.read1(0x0202));
    }
    
    @Test
    public void memoryMirrorTest() {
        /*
        LDA #42
        STA $0805
         */
        
        Console console = new Console();
        MemoryBus bus = console.getCpu().getBus();
        
        MemoryUtils.programWrite(bus, console.getCpu().getState(),
                0xA9, 42,
                0x8D, 0x05, 0x08);
        
        while (console.getCpu().tick());
        assertEquals(42, bus.read1(0x0005));
        assertEquals(42, bus.read1(0x0805));
        assertEquals(42, bus.read1(0x1005));
        assertEquals(42, bus.read1(0x1805));
    }
    
    @Test
    public void snakeTest() {
        //Test for debugging snake game
        Console console = new Console();
        MemoryBus bus = console.getCpu().getBus();
    
        //Set 0x10
        bus.write1(0x10, 0x15);
        bus.write1(0x11, 0x04);
        bus.write1(0x02, 0x01);
        MemoryUtils.programWrite(bus, console.getCpu().getState(),
                0xA5, 0x02,
                0x4A,
                0xB0, 1,
                0,
                0xA5, 0x10,
                0x38,
                0xE9, 0x20);
    
        while (console.getCpu().tick());
    }
    
    @Test
    public void sbcTest() {
        //Test for debugging snake game
        Console console = new Console();
        MemoryBus bus = console.getCpu().getBus();
    
        bus.write1(0x02, 0x01);
        MemoryUtils.programWrite(bus, console.getCpu().getState(),
                Operation.LDA_IMM.getOpcode(), 5,
                Operation.SBC_IMM.getOpcode(), 6);
    
        while (console.getCpu().tick());
        assertEquals(0xFF, console.getCpu().getState().regA);
    }
    
}
