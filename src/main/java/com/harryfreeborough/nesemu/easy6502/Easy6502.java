package com.harryfreeborough.nesemu.easy6502;

import com.harryfreeborough.nesemu.Console;
import com.harryfreeborough.nesemu.Cpu;
import com.harryfreeborough.nesemu.utils.MemoryUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Easy6502 {
    
    private final Console console;
    
    public Easy6502() {
        this.console = new Console();
        
        Cpu cpu = this.console.getCpu();
        
        String hexdump =
                "0600: 20 06 06 20 38 06 20 0d 06 20 2a 06 60 a9 02 85 \n" +
                        "0610: 02 a9 04 85 03 a9 11 85 10 a9 10 85 12 a9 0f 85 \n" +
                        "0620: 14 a9 04 85 11 85 13 85 15 60 a5 fe 85 00 a5 fe \n" +
                        "0630: 29 03 18 69 02 85 01 60 20 4d 06 20 8d 06 20 c3 \n" +
                        "0640: 06 20 19 07 20 20 07 20 2d 07 4c 38 06 a5 ff c9 \n" +
                        "0650: 77 f0 0d c9 64 f0 14 c9 73 f0 1b c9 61 f0 22 60 \n" +
                        "0660: a9 04 24 02 d0 26 a9 01 85 02 60 a9 08 24 02 d0 \n" +
                        "0670: 1b a9 02 85 02 60 a9 01 24 02 d0 10 a9 04 85 02 \n" +
                        "0680: 60 a9 02 24 02 d0 05 a9 08 85 02 60 60 20 94 06 \n" +
                        "0690: 20 a8 06 60 a5 00 c5 10 d0 0d a5 01 c5 11 d0 07 \n" +
                        "06a0: e6 03 e6 03 20 2a 06 60 a2 02 b5 10 c5 10 d0 06 \n" +
                        "06b0: b5 11 c5 11 f0 09 e8 e8 e4 03 f0 06 4c aa 06 4c \n" +
                        "06c0: 35 07 60 a6 03 ca 8a b5 10 95 12 ca 10 f9 a5 02 \n" +
                        "06d0: 4a b0 09 4a b0 19 4a b0 1f 4a b0 2f a5 10 38 e9 \n" +
                        "06e0: 20 85 10 90 01 60 c6 11 a9 01 c5 11 f0 28 60 e6 \n" +
                        "06f0: 10 a9 1f 24 10 f0 1f 60 a5 10 18 69 20 85 10 b0 \n" +
                        "0700: 01 60 e6 11 a9 06 c5 11 f0 0c 60 c6 10 a5 10 29 \n" +
                        "0710: 1f c9 1f f0 01 60 4c 35 07 a0 00 a5 fe 91 00 60 \n" +
                        "0720: a6 03 a9 00 81 10 a2 00 a9 01 81 10 60 a2 00 ea \n" +
                        "0730: ea ca d0 fb 60 ";
        
        List<Integer> instructions = new ArrayList<>();
        
        int n = 0;
        for (String line : hexdump.split("\n")) {
            line = line.substring(6, line.length() - 1);
            for (String instruction : line.split(" ")) {
                int opcode = Integer.parseUnsignedInt(instruction, 16);
                instructions.add(opcode);
                n++;
            }
        }
        
        int[] arr = instructions.stream().mapToInt(i -> i).toArray();
        MemoryUtils.programWrite(cpu.getBus(), cpu.getState(), arr);
    
        EasyFrame frame = new EasyFrame(cpu.getBus());
        
        while (cpu.tick()) {
            SwingUtilities.invokeLater(() -> {
                frame.validate();
                frame.repaint();
            });
        }
    }
    
    public static void main(String[] args) {
        new Easy6502();
    }
    
}
