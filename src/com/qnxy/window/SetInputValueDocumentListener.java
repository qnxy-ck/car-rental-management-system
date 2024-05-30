package com.qnxy.window;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * 输入框数据输入/删除事件
 *
 * @author Qnxy
 */
@FunctionalInterface
public interface SetInputValueDocumentListener extends DocumentListener {

    /**
     * 输入框输入内容响应事件
     *
     * @param inputValue 输入框的内容
     */
    void setInputValue(String inputValue);


    default void updateInputValue(DocumentEvent e) {
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
    default void insertUpdate(DocumentEvent e) {
        this.updateInputValue(e);
    }


    @Override
    default void removeUpdate(DocumentEvent e) {
        this.updateInputValue(e);
    }

    @Override
    default void changedUpdate(DocumentEvent e) {
    }
}
