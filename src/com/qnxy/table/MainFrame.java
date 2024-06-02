package com.qnxy.table;

import com.qnxy.table.panel.TableDemo04;

import javax.swing.*;
import java.awt.*;

/**
 * @author Qnxy
 */
public class MainFrame extends JFrame {

    public MainFrame() throws HeadlessException {
        setTitle("Table Demo 01");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        setContentPane(new TableDemo04());

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new MainFrame();
    }


}