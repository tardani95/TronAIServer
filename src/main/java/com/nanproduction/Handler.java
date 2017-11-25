package com.nanproduction;

import javafx.scene.input.KeyEvent;

public interface Handler {
    public void giveKeyEvent(KeyEvent keyEvent);
    public KeyEvent getKeyEvent();
}
