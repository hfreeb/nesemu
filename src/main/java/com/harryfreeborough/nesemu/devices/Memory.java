package com.harryfreeborough.nesemu.devices;

import com.harryfreeborough.nesemu.exceptions.BusException;

public class Memory implements Device {

    private final int startAddress;
    private final int size;
    private final boolean readOnly;
    private final byte[] data;

    public Memory(int startAddress, int size, boolean readOnly) {
        this.startAddress = startAddress;
        this.size = size;
        this.readOnly = readOnly;
        this.data = new byte[size];
    }

    @Override
    public int getStartAddress() {
        return this.startAddress;
    }

    @Override
    public int getSize() {
        return this.size;
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
