package com.harryfreeborough.nesemu.device;

public interface Device {
    
    /**
     * Returns the number of bytes used by the device.
     *
     * @return size of device in memory
     */
    int getLength();
    
    /**
     * Reads one byte of data at the specified address.
     *
     * @param address to read from
     * @return data at the specified address
     */
    int read(int address);
    
    /**
     * Writes one byte of data, specified by value at the specified address.
     *
     * @param address to write to
     * @param value   to write
     */
    void write(int address, int value);
    
}
