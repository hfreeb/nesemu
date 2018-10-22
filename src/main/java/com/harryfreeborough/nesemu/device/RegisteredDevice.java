package com.harryfreeborough.nesemu.device;

public class RegisteredDevice implements Comparable<RegisteredDevice> {
    
    private final Device device;
    private final int startAddress;
    
    public RegisteredDevice(Device device, int startAddress) {
        this.device = device;
        this.startAddress = startAddress;
    }
    
    public Device getDevice() {
        return this.device;
    }
    
    public int getStartAddress() {
        return this.startAddress;
    }
    
    @Override
    public int compareTo(RegisteredDevice other) {
        return Integer.compare(this.getStartAddress(), other.getStartAddress());
    }
    
}
