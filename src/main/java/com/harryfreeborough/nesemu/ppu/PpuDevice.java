package com.harryfreeborough.nesemu.ppu;

import com.harryfreeborough.nesemu.device.Device;

public class PpuDevice implements Device {

    @Override
    public int getLength() {
        return 8;
    }

    @Override
    public int read(int address) {
        switch (address) {
            case 2:
                System.out.println("PPUSTATUS");
                break;
            case 4:
                System.out.println("OAMDATAr");
            case 7:
                System.out.println("PPUDATAr");
        }

        return 0;
    }

    @Override
    public void write(int address, int value) {
        switch (address) {
            case 0:
                System.out.println("PPUCTRL");
                break;
            case 1:
                System.out.println("PPUMASK");
                break;
            case 3:
                System.out.println("OAMADDR");
                break;
            case 4:
                System.out.println("OAMDATAw");
                break;
            case 5:
                System.out.println("PPUSCROLL");
                break;
            case 6:
                System.out.println("PPUADDR");
                break;
            case 7:
                System.out.println("PPUDATAw");
                break;
        }
    }

}
