package com.qnxy.window;

import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * @author Qnxy
 */
@RequiredArgsConstructor
public class QuickListenerAdder {
    private final JComponent component;

    public <T extends AbstractButton> QuickListenerAdder add(T source, ActionListener listener) {
        source.addActionListener(listener);
        component.add(source);
        return this;
    }

}
