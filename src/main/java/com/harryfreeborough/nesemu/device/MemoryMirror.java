package com.harryfreeborough.nesemu.device;

public class MemoryMirror implements Device {
    
    private final MemoryBus bus;
    private final int length;
    private final int mirrorStart;
    private final int mirrorSize;
    
    public MemoryMirror(MemoryBus bus, int length, int mirrorStart, int mirrorSize) {
        this.bus = bus;
        this.length = length;
        this.mirrorStart = mirrorStart;
        this.mirrorSize = mirrorSize;
    }
    
    @Override
    public int getLength() {
        return this.length;
    }
    
    @Override
    public int read(int address) {
        return this.bus.read1(this.mirrorStart + (address % this.mirrorSize));
    }
    
    @Override
    public void write(int address, int value) {
        this.bus.write1(this.mirrorStart + (address % this.mirrorSize), value);
    }
    
}
