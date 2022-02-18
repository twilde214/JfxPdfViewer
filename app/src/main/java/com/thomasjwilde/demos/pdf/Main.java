package com.thomasjwilde.demos.pdf;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        FullWebViewPDF webViewPDFJS = new FullWebViewPDF();
        webViewPDFJS.getWebView().getEngine().load("http://www.africau.edu/images/default/sample.pdf");

        Scene scene = new Scene(webViewPDFJS, 800, 600);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
