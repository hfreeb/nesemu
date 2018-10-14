package com.harryfreeborough.nesemu.devices;

import com.harryfreeborough.nesemu.utils.Preconditions;

public interface Device extends Comparable<Device> {

    /**
     * Returns the start address in the bus of the device.
     *
     * @return start address of  device
     */
    int getStartAddress();

    /**
     * Returns the number of bytes used by the device.
     *
     * @return size of device in memory
     */
    int getSize();

    default boolean includes(int address) {
        return address >= getStartAddress() && address <= getStartAddress() + getSize();
    }

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
     * @param value to write
     */
    void write(int address, int value);

    @Override
    default int compareTo(Device other) {
        Preconditions.checkNotNull(other, "other");

        return Integer.compare(this.getStartAddress(), other.getStartAddress());
    }

}
