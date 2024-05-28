package com.qnxy.window;

import javax.swing.*;

/**
 * @author Qnxy
 */
public abstract class ChildPanelSupport extends JPanel {
    private static final String TITLE = "汽车租赁管理系统";

    private ApplicationFrameSupport rootFrame;


    protected void setRootFrame(ApplicationFrameSupport rootFrame) {
        this.rootFrame = rootFrame;
        initialization(new ParentFrameScope());
    }

    protected void removeThisAndAdd(ChildPanelSupport addComponent) {
        rootFrame.remove(ChildPanelSupport.this);
        rootFrame.addComponent(addComponent);
    }

    protected abstract void initialization(ParentFrameScope parentFrameScope);


    protected class ParentFrameScope {

        public void setDefaultCloseOperation(CloseOperation operation) {
            rootFrame.setDefaultCloseOperation(operation.typeNum);
        }

        /**
         * 窗口标题设置
         *
         * @param title 标题内容
         */
        public void setWindowTitle(String title) {
            rootFrame.setTitle(TITLE + " -> " + title);
        }
    }

    protected enum CloseOperation {
        EXIT_ON_CLOSE(WindowConstants.EXIT_ON_CLOSE),
        DO_NOTHING_ON_CLOSE(WindowConstants.DO_NOTHING_ON_CLOSE);

        private final int typeNum;

        CloseOperation(int typeNum) {
            this.typeNum = typeNum;
        }
    }
}
