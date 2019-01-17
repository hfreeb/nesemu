package com.harryfreeborough.nesemu.ppu;

public class Ppu {

    private int scanline = -1;

    public void runScanline() {
        if (this.scanline > 260) {
            //TODO: Complete frame
            return;
        }

        if (this.scanline == -1) { //Pre-render scanline

        } else if (this.scanline < 240) { //Visible scanlines
            int nametable =
        } else if (this.scanline == 240) { //Post-render scanline

        } else { //Vertical blanking lines

        }
    }

}
