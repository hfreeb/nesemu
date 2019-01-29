package com.harryfreeborough.nesemu.utils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileReader implements AutoCloseable {
    
    private final InputStream stream;
    private int currentByte;
    private int bitIndex;
    
    public FileReader(InputStream stream) throws IOException {
        this.stream = stream;
    }
    
    public int readByte() throws IOException {
        Preconditions.checkState(this.bitIndex == 0, "The current byte has not been iterated fully.");
        int value = this.stream.read();
        Preconditions.checkState(value != -1, "Failed to read byte from InputStream.");
        return value;
    }
    
    public byte[] readBytes(int n) throws IOException {
        Preconditions.checkState(this.bitIndex == 0, "The current byte has not been iterated fully.");
        byte[] data = new byte[n];
        int result = this.stream.read(data);
        Preconditions.checkState(result != -1, "Failed to read into byte array.");
        return data;
    }
    
    /**
     * Reads a single bit in a little-endian manner, i.e. least significant bit first.
     */
    public int readBit() throws IOException {
        if (this.bitIndex == 0) {
            this.currentByte = readByte();
        }
        
        int value = (this.currentByte >> this.bitIndex) & 0x01;
        
        if (this.bitIndex == 7) {
            this.bitIndex = 0;
        } else {
            this.bitIndex++;
        }
        
        return value;
    }
    
    public boolean readBoolean() throws IOException {
        return readBit() == 1;
    }
    
    public int readBits(int n) throws IOException {
        int value = 0;
        for (int i = 0; i < n; i++) {
            value |= readBit() << i;
        }
        return value;
    }
    
    @Override
    public void close() throws IOException {
        this.stream.close();
    }
    
}
