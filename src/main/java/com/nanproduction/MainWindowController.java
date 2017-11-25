package com.nanproduction;

import javafx.fxml.FXML;

public class MainWindowController {

    public MainWindowController() {
        System.out.println("MainWindowController() called");
    }

    @FXML
    void initialize(){
        System.out.println("Initialize() called");
    }
}
