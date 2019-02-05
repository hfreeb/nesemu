package com.harryfreeborough.nesemu.mapper;

import com.harryfreeborough.nesemu.rom.Cartridge;
import com.harryfreeborough.nesemu.rom.MirroringMode;

public class Mapper0 implements Mapper {

    private final Cartridge cartridge;

    public Mapper0(Cartridge cartridge) {
        this.cartridge = cartridge;
    }

    @Override
    public int read1(int address) {
        if (address < 0x2000) { //PPU memmory
            return Byte.toUnsignedInt(this.cartridge.getChrRomData()[address]);
        } else if (address >= 0x8000 && address < 0xC000) { //CPU memory
            return Byte.toUnsignedInt(this.cartridge.getPrgRomData()[address % 0x4000]);
        } else if (address >= 0xC000) {
            //Last 16KiB of ROM
            return Byte.toUnsignedInt(
                    this.cartridge.getPrgRomData()[(this.cartridge.getPrgRomSize() - 1) * 0x4000 + address % 0x4000]
            );
        }

        throw new IllegalArgumentException(String.format("Failed to read from address $%04X", address));
    }

    @Override
    public void write1(int address, int value) {
        throw new IllegalArgumentException(String.format("Failed to write to address $%04X", address));
    }

    @Override
    public MirroringMode getMirroringMode() {
        return this.cartridge.getMirroringMode();
    }

}
