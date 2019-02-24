package com.harryfreeborough.nesemu.mapper;

import com.harryfreeborough.nesemu.rom.MirroringMode;
import com.harryfreeborough.nesemu.utils.MemorySpace;

/**
 * Handles all the custom circuitry for different NES cartridges.
 */
public interface Mapper extends MemorySpace {

    @Override
    default void write1(int address, int value) {
        throw new IllegalArgumentException(String.format("Failed to write to address $%04X", address));
    }

    MirroringMode getMirroringMode();

}
