package com.harryfreeborough.nesemu.device;

import com.harryfreeborough.nesemu.exceptions.BusException;
import com.harryfreeborough.nesemu.utils.Preconditions;

import java.util.NavigableSet;
import java.util.Optional;
import java.util.TreeSet;

public class MemoryBus {
    
    private final NavigableSet<RegisteredDevice> devices;
    
    public MemoryBus() {
        this.devices = new TreeSet<>();
    }
    
    public void registerDevice(Device device, int startAddress) {
        int end = startAddress + device.getLength() - 1;
        
        for (RegisteredDevice other : this.devices) {
            int otherStart = other.getStartAddress();
            int otherEnd = other.getStartAddress() + other.getDevice().getLength() - 1;
            
            Preconditions.checkState((startAddress < otherStart && end < otherStart) || (startAddress > otherEnd),
                    "Memory overlap between two devices: %04X with length %04X and %04X with length %04X",
                    startAddress, device.getLength(), otherStart, other.getDevice().getLength());
        }
        
        this.devices.add(new RegisteredDevice(device, startAddress));
    }
    
    public RegisteredDevice getMemoryHolder(int address) {
        //TODO: Better search
        return this.devices.stream()
                .filter(device -> address >= device.getStartAddress() &&
                        address < (device.getStartAddress() + device.getDevice().getLength()))
                .findAny()
                .orElseThrow(() -> new BusException("Failed to get memory holder of address: $%04X", address));
    }
    
    public int read1(int address) {
        address &= 0xFFFF;
        
        RegisteredDevice device = getMemoryHolder(address);
        return device.getDevice().read(address - device.getStartAddress()) & 0xFF;
    }
    
    public int read2(int address) {
        RegisteredDevice device = getMemoryHolder(address);
        int relativeAddress = (address - device.getStartAddress()) & 0xFFFF;
        int part1 = device.getDevice().read(relativeAddress) & 0xFF;
        int part2 = ((device.getDevice().read(relativeAddress + 1) & 0xFF) << 8);
        return part1 | part2;
    }
    
    public void write1(int address, int value) {
        RegisteredDevice device = getMemoryHolder(address);
        
        int relativeAddress = (address - device.getStartAddress()) & 0xFFFF;
        device.getDevice().write(relativeAddress, value & 0xFF);
    }
    
    public void write2(int address, int value) {
        RegisteredDevice device = getMemoryHolder(address);
        
        int relativeAddress = (address - device.getStartAddress()) & 0xFFFF;
        device.getDevice().write(relativeAddress, value & 0xFF);
        device.getDevice().write(relativeAddress + 1, value >> 8);
    }
    
}
