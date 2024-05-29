package com.qnxy.window;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * @author Qnxy
 */
public class QuickListenerAdder {
    private final JComponent component;

    public QuickListenerAdder(JComponent parent) {
        this.component = parent;
    }

    public <T extends AbstractButton> QuickListenerAdder add(T source, ActionListener listener) {
        source.addActionListener(listener);
        component.add(source);
        return this;
    }

}
