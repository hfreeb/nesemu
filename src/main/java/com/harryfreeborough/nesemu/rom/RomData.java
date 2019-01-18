package com.harryfreeborough.nesemu.rom;

import javax.annotation.Nullable;
import java.util.Optional;

public class RomData {
    
    private final MirroringType mirroringType;
    private final boolean persistentMemory;
    private final int mapperId;
    private final int prgRomSize;
    private final int chrRomSize;
    @Nullable private final byte[] trainerData;
    private final byte[] prgRomData;
    private final byte[] chrRomData;
    
    RomData(MirroringType mirroringType, boolean persistentMemory,
                int mapperId, int prgRomSize, int chrRomSize,
                @Nullable byte[] trainerData, byte[] prgRomData, byte[] chrRomData) {
        this.mirroringType = mirroringType;
        this.persistentMemory = persistentMemory;
        this.mapperId = mapperId;
        this.prgRomSize = prgRomSize;
        this.chrRomSize = chrRomSize;
        this.trainerData = trainerData;
        this.prgRomData = prgRomData;
        this.chrRomData = chrRomData;
    }
    
    public MirroringType getMirroringType() {
        return this.mirroringType;
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
    
}
