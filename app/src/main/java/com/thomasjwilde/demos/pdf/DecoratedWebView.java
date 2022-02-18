package com.thomasjwilde.demos.pdf;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public abstract class DecoratedWebView extends BorderPane {

    protected TextField textField = new TextField();
    protected Button backButton = new Button("Back");
    protected Button forwardButton = new Button("Forward");
    protected Button goButton = new Button("Go");


    public DecoratedWebView(){
        setPadding(new Insets(5));

        backButton.setOnAction(event -> goBack());
        forwardButton.setOnAction(event -> goForward());
        textField.setOnAction(event -> loadURL(textField.getText()));
        goButton.setOnAction(event -> loadURL(textField.getText()));

        HBox.setHgrow(textField, Priority.ALWAYS);
        HBox hBox = new HBox(backButton, forwardButton, textField, goButton);
        hBox.setSpacing(5);
        hBox.setPadding(new Insets(5));

        setTop(hBox);
    }

    public abstract void goBack();
    public abstract void goForward();
    public abstract void loadURL(String text);

    public TextField getTextField() {
        return textField;
    }
}
