package com.harryfreeborough.nesemu.ui;

import com.harryfreeborough.nesemu.Console;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EmuKeyListener implements KeyListener {

    //Maps Java Swing KeyEvent codes to NES key indices.
    private static final Map<Integer, Integer> KEY_MAPPING;

    static {
        Map<Integer, Integer> mapping = new HashMap<>();
        mapping.put(KeyEvent.VK_Z, 0);
        mapping.put(KeyEvent.VK_X, 1);
        mapping.put(KeyEvent.VK_SPACE, 2);
        mapping.put(KeyEvent.VK_ENTER, 3);
        mapping.put(KeyEvent.VK_UP, 4);
        mapping.put(KeyEvent.VK_DOWN, 5);
        mapping.put(KeyEvent.VK_LEFT, 6);
        mapping.put(KeyEvent.VK_RIGHT, 7);

        KEY_MAPPING = Collections.unmodifiableMap(mapping);
    }

    private final Console console;

    public EmuKeyListener(Console console) {
        this.console = console;
    }

    @Override
    public void keyTyped(KeyEvent event) {
        //ignored
    }

    @Override
    public void keyPressed(KeyEvent event) {
        Integer button = KEY_MAPPING.get(event.getKeyCode());
        if (button != null) {
            this.console.getCpu().getMemory().setButtonDown(button, true);
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {
        Integer button = KEY_MAPPING.get(event.getKeyCode());
        if (button != null) {
            this.console.getCpu().getMemory().setButtonDown(button, false);
            return;
        }

        switch (event.getKeyCode()) {
            case KeyEvent.VK_S:
                this.console.queueSave();
                break;
            case KeyEvent.VK_L:
                this.console.queueLoad();
                break;
            case KeyEvent.VK_R:
                this.console.queueReset();
                break;
        }
    }
}
