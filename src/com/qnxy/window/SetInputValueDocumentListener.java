package com.qnxy.window;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * @author Qnxy
 */
@FunctionalInterface
public interface SetInputValueDocumentListener extends DocumentListener {

    void setInputValue(String inputValue);

    @Override
    default void insertUpdate(DocumentEvent e) {
        final Document document = e.getDocument();
        final int length = document.getLength();
        try {
            final String text = document.getText(0, length);
            setInputValue(text);
        } catch (BadLocationException ex) {
            // ignore
        }

    }


    @Override
    default void removeUpdate(DocumentEvent e) {
        Document document = e.getDocument();
        int length = e.getLength();
        try {
            setInputValue(document.getText(0, length));

        } catch (BadLocationException ex) {
            // ignore
        }
    }

    @Override
    default void changedUpdate(DocumentEvent e) {
    }
}
