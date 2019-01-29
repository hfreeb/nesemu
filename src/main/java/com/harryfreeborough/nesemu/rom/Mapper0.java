package com.harryfreeborough.nesemu.rom;

import com.harryfreeborough.nesemu.Console;

public class Mapper0 implements Mapper {

    private final Console console;

    public Mapper0(Console console) {
        this.console = console;
    }

    @Override
    public int read1(int address) {
        Cartridge cartridge = this.console.getCartridge();

        if (address < 0x2000) { //PPU memmory
            return Byte.toUnsignedInt(cartridge.getChrRomData()[address]);
        } else if (address >= 0x8000 && address < 0xC000) { //CPU memory
            return Byte.toUnsignedInt(cartridge.getPrgRomData()[address % 0x4000]);
        } else if (address >= 0xC000) {
            //Last 16KiB of ROM
            return Byte.toUnsignedInt(
                    cartridge.getPrgRomData()[(cartridge.getPrgRomSize() - 1) * 0x4000 + address % 0x4000]
            );
        }

        throw new IllegalArgumentException(String.format("Failed to read from address $%04X", address));
    }

    @Override
    public void write1(int address, int value) {
        if (address < 0x2000) { //PPU memory
            this.console.getCartridge().getChrRomData()[address] = (byte) value;
        } else {
            throw new IllegalArgumentException(String.format("Failed to write to address $%04X", address));
        }
    }
}
