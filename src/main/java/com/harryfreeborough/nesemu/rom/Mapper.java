package com.harryfreeborough.nesemu.rom;

import com.harryfreeborough.nesemu.utils.Memory;

public interface Mapper extends Memory  {

    @Override
    default void write1(int address, int value) {
        throw new IllegalArgumentException(String.format("Failed to write to address $%04X", address));
    }

}
