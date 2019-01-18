package com.harryfreeborough.nesemu.ppu;

public class Ppu {

    private int scanline = -1;

    public int readRegister(int address) {
        switch (address) {
            case 0x2002:
                System.out.println("PPUSTATUS");
                break;
            case 0x2004:
                System.out.println("OAMDATAr");
            case 0x2007:
                System.out.println("PPUDATAr");
        }

        return 0;
    }

    public void writeRegister(int address, int value) {
        switch (address) {
            case 0x2000:
                System.out.println("PPUCTRL");
                break;
            case 0x2001:
                System.out.println("PPUMASK");
                break;
            case 0x2003:
                System.out.println("OAMADDR");
                break;
            case 0x2004:
                System.out.println("OAMDATAw");
                break;
            case 0x2005:
                System.out.println("PPUSCROLL");
                break;
            case 0x2006:
                System.out.println("PPUADDR");
                break;
            case 0x2007:
                System.out.println("PPUDATAw");
                break;
        }
    }

    public void runScanline() {
        if (this.scanline > 260) {
            //TODO: Complete frame
            return;
        }

        if (this.scanline == -1) { //Pre-render scanline

        } else if (this.scanline < 240) { //Visible scanlines
        } else if (this.scanline == 240) { //Post-render scanline

        } else { //Vertical blanking lines

        }
    }

}
