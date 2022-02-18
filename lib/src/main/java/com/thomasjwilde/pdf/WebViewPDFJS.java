package com.thomasjwilde.pdf;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Worker;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

public class WebViewPDFJS extends StackPane {

    private WebView webView;
    private StringProperty pseudoURL = new SimpleStringProperty(this, "pseudoURL");
    private String pseudoURLShadow;
    private Path localPDFPath;

    public WebViewPDFJS(){
        webView = new WebView();
        getChildren().add(webView);
        WebEngine webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        webEngine.locationProperty().addListener((obs, oldLocation, newLocation) -> {
            if (newLocation != null && newLocation.startsWith("file:") && newLocation.endsWith(".pdf")) {
                System.out.println("new location was local pdf: " + newLocation);
                pseudoURLShadow = newLocation;
                try {
                    localPDFPath = Paths.get(new URL(pseudoURLShadow).toURI());
                } catch (URISyntaxException | MalformedURLException e) {
                    e.printStackTrace();
                }

                webView.getEngine().load(getPDFJSViewer());
            }else if(newLocation != null && newLocation.startsWith("http") && newLocation.endsWith(".pdf")){
                localPDFPath = Paths.get(System.getProperty("user.home")).resolve("doc.pdf");
                try {
                    copyFileToTarget(newLocation, localPDFPath);
                    pseudoURLShadow = newLocation;
                    webView.getEngine().load(getPDFJSViewer());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(newLocation != null && !newLocation.startsWith("file:") && !newLocation.startsWith("http")){
                webView.getEngine().load("http://" + newLocation);
            }
        });
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                if(webEngine.getLocation().endsWith("web/viewer.html")){
                    if(pseudoURLShadow != null){
                        try {
                            navigateToLocalPDF(localPDFPath);
                            System.out.println("setting pseudo url to " + pseudoURLShadow);
                            pseudoURL.set(pseudoURLShadow);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        });
    }

    public String getPDFJSViewer(){
        URL url = getClass().getResource("/com/thomasjwilde/pdf/web/viewer.html");
        try {
            return url.toURI().toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void navigateToPdfViewer(){
        URL url = getClass().getResource("/com/thomasjwilde/pdf/web/viewer.html");
        if(url == null){
            System.out.println("url is null");
        }else{
            System.out.println("url is not null");
        }
//        webView.getEngine().load("file:///C:/Users/Thomas/Downloads/wildepdf/web/viewer.html");
        webView.getEngine().load("file:///C:/Users/Thomas/Downloads/compressed.tracemonkey-pldi-09.pdf");
    }

    public void navigateToLocalPDF(Path path) throws IOException {
        byte[] fileContent = Files.readAllBytes(path);
        String base64 = Base64.getEncoder().encodeToString(fileContent);
        webView.getEngine().executeScript("openFileFromBase64('" + base64 + "')");
    }

    public WebView getWebView() {
        return webView;
    }

    public String getPseudoURL() {
        return pseudoURL.get();
    }

    public StringProperty pseudoURLProperty() {
        return pseudoURL;
    }

    public void setPseudoURL(String pseudoURL) {
        this.pseudoURL.set(pseudoURL);
    }

    public void copyFileToTarget(String fileURL, Path targetPath) throws IOException {
        InputStream in = new URL(fileURL).openStream();
        Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
        in.close();
    }
}
