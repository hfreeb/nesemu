package com.harryfreeborough.nesemu.ui;

import com.harryfreeborough.nesemu.Console;

import javax.swing.*;
import java.awt.*;

public class EmuFrame extends JFrame {

    public EmuFrame(Console console) {
        super();

        JPanel panel = new EmuPanel(console);
        getContentPane().add(panel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(256 * 2, 240 * 2));
        setMinimumSize(new Dimension(256 * 2, 240 * 2));
        pack();
        setVisible(true);
    }

}
