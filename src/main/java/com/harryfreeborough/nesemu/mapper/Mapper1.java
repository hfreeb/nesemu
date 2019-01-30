package com.harryfreeborough.nesemu.mapper;

import com.harryfreeborough.nesemu.rom.Cartridge;
import com.harryfreeborough.nesemu.rom.MirroringMode;
import com.harryfreeborough.nesemu.utils.Preconditions;

public class Mapper1 implements Mapper {
    
    private final Cartridge cartridge;
    
    private MirroringMode mirroringMode;
    private int shiftReg = 0b10000;
    private int controlReg; //Only stored for resetting with control = (control | 0xC0)
    
    //0, 1: switch all 32KiB, 2: fix lower and switch upper, 3: fix upper bank and switch lower
    private int prgBankMode;
    //0: switch single 8KiB bank, 1: switch two separate 4KiB banks
    private int chrBankMode;
    
    private int chrBank0;
    private int chrBank1;
    private int prgBank;
    
    public Mapper1(Cartridge cartridge) {
        this.cartridge = cartridge;
        this.mirroringMode = this.cartridge.getMirroringMode();

        writeControl(0x0C);
    }
    
    @Override
    public int read1(int address) {
        if (address < 0x2000) {
            int bank;
            if (this.chrBankMode == 0) {
                if (address < 0x1000) {
                    bank = this.chrBank0 & 0b1110;
                } else {
                    bank = this.chrBank0 | 0b0001;
                }
            } else {
                if (address < 0x1000) {
                    bank = this.chrBank0;
                } else {
                    bank = this.chrBank1;
                }
            }
    
            //bank %= this.cartridge.getChrRomData().length / 0x1000;
    
            return Byte.toUnsignedInt(this.cartridge.getChrRomData()[0x1000 * bank + (address % 0x1000)]);
        } else if (address < 0x8000) {
            return Byte.toUnsignedInt(this.cartridge.getSaveRamData()[address - 0x6000]);
        } else if (address <= 0xFFFF) {
            int bank = 0;
            if (this.prgBankMode == 0 || this.prgBankMode == 1) {
                if (address < 0xC000) {
                    bank = this.prgBank & 0b1110;
                } else {
                    bank = this.prgBank | 0b0001;
                }
            } else if (this.prgBankMode == 2 && address >= 0xC000) {
                bank = this.prgBank;
            } else if (this.prgBankMode == 3) {
                if (address < 0xC000) {
                    bank = this.prgBank;
                } else {
                    bank = this.cartridge.getPrgRomSize() - 1;
                }
            }
            
            //bank %= this.cartridge.getPrgRomData().length / 0x4000;
            
            return Byte.toUnsignedInt(this.cartridge.getPrgRomData()[bank * 0x4000 + (address % 0x4000)]);
        }

        throw new IllegalStateException(String.format("Failed to read from address $%04X", address));
    }
    
    @Override
    public void write1(int address, int value) {
        if (address >= 0x6000 && address < 0x8000) {
            this.cartridge.getSaveRamData()[address - 0x6000] = (byte) value;
        } else if (address >= 0x8000) {
            if ((value >> 7) == 1) { //Reset
                this.shiftReg = 0b10000;
                writeControl(this.controlReg | 0x0C);
            } else {
                boolean push = (this.shiftReg & 0x01) == 1;
                this.shiftReg >>= 1;
                this.shiftReg |= (value & 1) << 4;
                
                if (!push) {
                    return;
                }
                
                if (address < 0xA000) {
                    writeControl(this.shiftReg);
                } else if (address < 0xC000) {
                    this.chrBank0 = this.shiftReg;
                } else if (address < 0xE000) {
                    this.chrBank1 = this.shiftReg;
                } else {
                    Preconditions.checkState((this.shiftReg & 0b10000) == 0, "PRG RAM chip not supported");
                    
                    this.prgBank = this.shiftReg & 0b01111; //Ignore PRG RAM chip enable
                }
                
                this.shiftReg = 0b10000;
            }
        } else {
            throw new IllegalArgumentException(String.format("Failed to write to address $%04X", address));
        }
    }
    
    @Override
    public MirroringMode getMirroringMode() {
        return this.mirroringMode;
    }
    
    private void writeControl(int value) {
        this.controlReg = value;
        
        switch (value & 0x03) {
            case 0:
                this.mirroringMode = MirroringMode.ONE_SCREEN_LOW;
                break;
            case 1:
                this.mirroringMode = MirroringMode.ONE_SCREEN_UPPER;
                break;
            case 2:
                this.mirroringMode = MirroringMode.VERTICAL;
                break;
            case 3:
                this.mirroringMode = MirroringMode.HORIZONTAL;
                break;
        }
        
        this.prgBankMode = (value & 0b01100) >> 2;
        this.chrBankMode = (value & 0b10000) >> 4;
    }
    
}
