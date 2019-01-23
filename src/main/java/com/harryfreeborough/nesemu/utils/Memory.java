package com.harryfreeborough.nesemu.utils;

public interface Memory {
    
    int read1(int address);
    
    default int read2(int address) {
        return read1(address) | (read1(address + 1) << 8);
    }
    
    void write1(int address, int value);
    
    default void write2(int address, int value) {
        write1(address, value & 0xFF);
        write1(address + 1, value >> 8); //TODO: check
    }
    
}
