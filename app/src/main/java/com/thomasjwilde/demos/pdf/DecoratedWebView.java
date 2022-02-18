package com.thomasjwilde.demos.pdf;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public abstract class DecoratedWebView extends BorderPane {

    protected TextField textField = new TextField();

    public DecoratedWebView(){
        setPadding(new Insets(5));

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> goBack());
        Button forwardButton = new Button("Forward");
        forwardButton.setOnAction(event -> goForward());

        textField.setOnAction(event -> loadURL(textField.getText()));
        Button goButton = new Button("Go");
        goButton.setOnAction(event -> loadURL(textField.getText()));

        HBox.setHgrow(textField, Priority.ALWAYS);
        HBox hBox = new HBox(backButton, forwardButton, textField, goButton);
        hBox.setSpacing(5);

        setTop(hBox);
    }

    public abstract void goBack();
    public abstract void goForward();
    public abstract void loadURL(String text);

    public TextField getTextField() {
        return textField;
    }
}
