package com.harryfreeborough.nesemu.device;

import com.harryfreeborough.nesemu.easy6502.EasyFrame;
import com.harryfreeborough.nesemu.exceptions.BusException;

import java.util.concurrent.ThreadLocalRandom;

public class Memory implements Device {
    
    private final int length;
    private final boolean readOnly;
    private final byte[] data;
    
    public Memory(int length, boolean readOnly) {
        this.length = length;
        this.readOnly = readOnly;
        this.data = new byte[length];
    }
    
    @Override
    public int getLength() {
        return this.length;
    }
    
    @Override
    public int read(int address) {
        //TODO: Remove
        if (address == 0xFE) { //Temporary, for Easy6502
            return ThreadLocalRandom.current().nextInt(0, 256);
        } else if (address == 0xFF) {
            return EasyFrame.lastKey;
        }
        
        return this.data[address];
    }
    
    @Override
    public void write(int address, int value) {
        if (!this.readOnly) {
            this.data[address] = (byte) value;
            return;
        }
        
        throw new BusException("You can not write to ROM.");
    }
    
}
