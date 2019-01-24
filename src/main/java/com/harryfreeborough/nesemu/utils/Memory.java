package com.harryfreeborough.nesemu.utils;

public interface Memory {
    
    int read1(int address);
    
    default int read2(int address) {
        int lsb = read1(address);
        int msb = read1(address + 1);
        return (msb << 8) | lsb;
    }
    
    default int read2Bug(int address) {
        int a = address;
        int b = (a & 0xFF00) | (((a & 0xFF) + 1) & 0xFF);
        int lsb = read1(a);
        int msb = read1(b);
        return (msb << 8) | lsb;
    }
    
    void write1(int address, int value);
    
    default void write2(int address, int value) {
        write1(address, value & 0xFF);
        write1(address + 1, value >> 8); //TODO: check
    }
    
}
