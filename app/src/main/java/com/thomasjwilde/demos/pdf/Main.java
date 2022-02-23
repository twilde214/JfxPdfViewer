package com.thomasjwilde.demos.pdf;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class Main extends Application {

    private static Logger log = LogManager.getLogger();

    @Override
    public void start(Stage stage) throws IOException {

        log.debug("JavaFX application started");

        FullWebViewPDF webViewPDFJS = new FullWebViewPDF();
        webViewPDFJS.getWebView().getEngine().load("https://s2.q4cdn.com/498544986/files/doc_downloads/test.pdf");

        Scene scene = new Scene(webViewPDFJS, 800, 600);
        stage.setTitle("JavaFX WebView Wrapper that supports PDFs");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread t, Throwable e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String stacktrace = sw.toString();
                log.fatal(stacktrace);
            }
        });
        launch();
    }
}
