package com.harryfreeborough.nesemu.rom;

import java.util.Optional;

import javax.annotation.Nullable;

public class Cartridge {

    private final MirroringMode mirroringMode;
    private final boolean persistentMemory;
    private final int mapperId;
    private final int prgRomSize;
    private final int chrRomSize;
    @Nullable
    private final byte[] trainerData;
    private final byte[] prgRomData;
    private final byte[] chrRomData;
    private final byte[] saveRamData;

    Cartridge(MirroringMode mirroringMode, boolean persistentMemory,
            int mapperId, int prgRomSize, int chrRomSize,
            @Nullable byte[] trainerData, byte[] prgRomData, byte[] chrRomData) {
        this.mirroringMode = mirroringMode;
        this.persistentMemory = persistentMemory;
        this.mapperId = mapperId;
        this.prgRomSize = prgRomSize;
        this.chrRomSize = chrRomSize;
        this.trainerData = trainerData;
        this.prgRomData = prgRomData;
        this.chrRomData = chrRomData;
        this.saveRamData = new byte[0x2000];
    }

    public MirroringMode getMirroringMode() {
        return this.mirroringMode;
    }

    public boolean isPersistentMemory() {
        return this.persistentMemory;
    }

    public int getMapperId() {
        return this.mapperId;
    }

    public int getPrgRomSize() {
        return this.prgRomSize;
    }

    public int getChrRomSize() {
        return this.chrRomSize;
    }

    public Optional<byte[]> getTrainerData() {
        return Optional.ofNullable(this.trainerData);
    }

    public byte[] getPrgRomData() {
        return this.prgRomData;
    }

    public byte[] getChrRomData() {
        return this.chrRomData;
    }

    public byte[] getSaveRamData() {
        return this.saveRamData;
    }

}
