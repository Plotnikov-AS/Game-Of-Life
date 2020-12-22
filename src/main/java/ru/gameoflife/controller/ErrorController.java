package ru.gameoflife.controller;

import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.stage.*;

public class ErrorController implements Controller{
    public Label lblErrorMsg;
    public Button btnOk;

    public void setErrorMsg(String errorMsg) {
        lblErrorMsg.setText(errorMsg);
        lblErrorMsg.setWrapText(true);
        lblErrorMsg.setTextAlignment(TextAlignment.LEFT);
        lblErrorMsg.setMaxWidth(300);

    }

    public void closeWindow() {
        Stage stage = (Stage) btnOk.getScene().getWindow();
        stage.close();
    }
}