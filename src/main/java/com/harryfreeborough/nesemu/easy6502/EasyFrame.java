package com.harryfreeborough.nesemu.easy6502;

import com.harryfreeborough.nesemu.device.MemoryBus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class EasyFrame extends JFrame implements KeyListener {
    
    public static int lastKey;
    
    private final MemoryBus memoryBus;
    
    public EasyFrame(MemoryBus memoryBus) {
        this.memoryBus = memoryBus;
        
        setMinimumSize(new Dimension(640, 640));
        JPanel panel = new EasyPanel(memoryBus);
        getContentPane().add(panel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.addKeyListener(this);
        pack();
        setVisible(true);
    }
    
    @Override
    public void keyTyped(KeyEvent keyEvent) {
        lastKey = keyEvent.getKeyChar();
    }
    
    @Override
    public void keyPressed(KeyEvent keyEvent) {
    }
    
    @Override
    public void keyReleased(KeyEvent keyEvent) {
    }
}
