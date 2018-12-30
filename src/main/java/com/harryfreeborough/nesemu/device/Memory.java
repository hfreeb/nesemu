package com.harryfreeborough.nesemu.device;

import com.harryfreeborough.nesemu.exceptions.BusException;

public class Memory implements Device {
    
    private final boolean readOnly;
    private final byte[] data;
    
    public Memory(int length, boolean readOnly) {
        this.data = new byte[length];
        this.readOnly = readOnly;
    }
    
    public Memory(byte[] data, boolean readOnly) {
        this.data = data;
        this.readOnly = readOnly;
    }
    
    @Override
    public int getLength() {
        return this.data.length;
    }
    
    @Override
    public int read(int address) {
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
