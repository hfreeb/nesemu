package com.harryfreeborough.nesemu.easy6502;

import com.harryfreeborough.nesemu.device.MemoryBus;

import javax.swing.*;
import java.awt.*;

public class EasyPanel extends JPanel {
    
    private final MemoryBus bus;
    
    public EasyPanel(MemoryBus bus) {
        super();
        this.bus = bus;
        setSize(640, 640);
        setBorder(BorderFactory.createEtchedBorder());
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        for (int x = 0; x < 32; x++) {
            for (int y = 0; y < 32; y++) {
                switch (this.bus.read1(0x200 + y * 32 + x) % 0x0D) {
                    case 0x00:
                        g.setColor(Color.BLACK);
                        break;
                    case 0x01:
                        g.setColor(Color.WHITE);
                        break;
                    case 0x02:
                        g.setColor(Color.LIGHT_GRAY);
                        break;
                    case 0x03:
                        g.setColor(Color.GRAY);
                        break;
                    case 0x04:
                        g.setColor(Color.DARK_GRAY);
                        break;
                    case 0x05:
                        g.setColor(Color.RED);
                        break;
                    case 0x06:
                        g.setColor(Color.PINK);
                        break;
                    case 0x07:
                        g.setColor(Color.ORANGE);
                        break;
                    case 0x08:
                        g.setColor(Color.YELLOW);
                        break;
                    case 0x09:
                        g.setColor(Color.GREEN);
                        break;
                    case 0x0A:
                        g.setColor(Color.MAGENTA);
                        break;
                    case 0x0B:
                        g.setColor(Color.CYAN);
                        break;
                    case 0x0C:
                        g.setColor(Color.BLUE);
                        break;
                    default:
                        g.setColor(Color.BLACK);
                        break;
                }
                
                g.fillRect(x * 20, y * 20, 20, 20);
            }
        }
    }
    
}
