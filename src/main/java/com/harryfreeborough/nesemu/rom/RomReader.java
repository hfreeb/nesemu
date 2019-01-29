package com.harryfreeborough.nesemu.rom;

import com.harryfreeborough.nesemu.utils.FileReader;
import com.harryfreeborough.nesemu.utils.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RomReader {

    public static Optional<Cartridge> read(InputStream stream) {
        try (FileReader reader = new FileReader(stream)) {
            int[] verification = {'N', 'E', 'S', 0x1A};
            for (int i = 0; i < verification.length; i++) {
                Preconditions.checkState(reader.readByte() == verification[i], "Failed to verify file format");
            }
            
            int prgRomSize = reader.readByte();
            int chrRomSize = reader.readByte();
            
            MirroringMode mirroringMode = reader.readBoolean() ? MirroringMode.VERTICAL : MirroringMode.HORIZONTAL;
            boolean persistentMemory = reader.readBoolean();
            boolean trainer = reader.readBoolean();
            if (reader.readBoolean()) {
                mirroringMode = MirroringMode.FOUR_SCREEN;
            }
            int mapperId = reader.readBits(4);
            boolean vsUnisystem = reader.readBoolean();
            boolean playChoice10 = reader.readBoolean();
            boolean formatNes2 = reader.readBits(2) == 0x02;
            mapperId |= reader.readBits(4) << 4;
            
            reader.readBytes(8); //Ignore data (even though it is all 0 for our rom)
            
            byte[] trainerData = null;
            if (trainer) {
                trainerData = reader.readBytes(512);
            }
            
            byte[] prgRomData = reader.readBytes(0x4000 * prgRomSize);
            byte[] chrRomData = reader.readBytes(0x2000 * chrRomSize);
            
            return Optional.of(
                    new Cartridge(mirroringMode, persistentMemory, mapperId, prgRomSize, chrRomSize,
                                trainerData, prgRomData, chrRomData)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return Optional.empty();
    }

}
