package com.harryfreeborough.nesemu.ui;

import com.harryfreeborough.nesemu.cpu.CpuState;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class EmuKeyListener implements KeyListener {

    private final CpuState cpuState;

    public EmuKeyListener(CpuState cpuState) {
        this.cpuState = cpuState;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        boolean[] state = this.cpuState.buttonState;
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_Z:
                state[0] = true;
                break;
            case KeyEvent.VK_X:
                state[1] = true;
                break;
            case KeyEvent.VK_SPACE:
                state[2] = true;
                break;
            case KeyEvent.VK_ENTER:
                state[3] = true;
                break;
            case KeyEvent.VK_UP:
                state[4] = true;
                break;
            case KeyEvent.VK_DOWN:
                state[5] = true;
                break;
            case KeyEvent.VK_LEFT:
                state[6] = true;
                break;
            case KeyEvent.VK_RIGHT:
                state[7] = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        boolean[] state = this.cpuState.buttonState;

        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_Z:
                state[0] = false;
                break;
            case KeyEvent.VK_X:
                state[1] = false;
                break;
            case KeyEvent.VK_SPACE:
                state[2] = false;
                break;
            case KeyEvent.VK_ENTER:
                state[3] = false;
                break;
            case KeyEvent.VK_UP:
                state[4] = false;
                break;
            case KeyEvent.VK_DOWN:
                state[5] = false;
                break;
            case KeyEvent.VK_LEFT:
                state[6] = false;
                break;
            case KeyEvent.VK_RIGHT:
                state[7] = false;
                break;
        }
    }
}
