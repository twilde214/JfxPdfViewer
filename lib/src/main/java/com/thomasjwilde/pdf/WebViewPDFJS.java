package com.thomasjwilde.pdf;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

/**
 * WebViewPDFJS is a wrapper class of WebView which allows the WebView to display pdfs.  The actual url of the WebView can be tracked using the actualURL property.
 */
public class WebViewPDFJS{

    private static Logger log = LoggerFactory.getLogger(WebViewPDFJS.class);

    private WebView webView;
    private final StringProperty actualURL = new SimpleStringProperty(this, "pseudoURL");
    private Path localPDFPath;

    public WebViewPDFJS(){
        log.debug("WebViewPDFJS constructed");
        webView = new WebView();
        init();
    }

    /**
     * Constructor to allow passing of user's own WebView
     * @param webView User defined WebView
     */
    public WebViewPDFJS(WebView webView){
        this.webView = new WebView();
        init();
    }

    private void init(){
        WebEngine webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        webEngine.locationProperty().addListener((obs, oldLocation, newLocation) -> {

            log.trace("Navigating to new location {}", newLocation);
            // A local pdf can be navigated to directly
            if (newLocation != null && newLocation.startsWith("file:") && newLocation.endsWith(".pdf")) {
                setActualURL(newLocation);

                try {
                    localPDFPath = Paths.get(new URL(newLocation).toURI());
                    webView.getEngine().load(getPDFJSViewer());
                } catch (URISyntaxException | MalformedURLException e) {
                    log.error(e.getMessage());
                    e.printStackTrace();
                }

            }

            // Pdf on the web needs to be downloaded to temp, then navigated to
            else if(newLocation != null && newLocation.startsWith("http") && newLocation.endsWith(".pdf")){
                localPDFPath = Paths.get(System.getProperty("user.home")).resolve("doc.pdf");
                copyFileToTarget(newLocation, localPDFPath);
                setActualURL(newLocation);
                webView.getEngine().load(getPDFJSViewer());

            }

            // WebView needs to start with https://
            else if(newLocation != null && !newLocation.startsWith("file:") && !newLocation.startsWith("http") && !newLocation.startsWith("jrt:")){
                webView.getEngine().load("https://" + newLocation);
            }

            // If the new location is the pdf.js viewer, don't update the actual url
            else if (newLocation != null && !newLocation.startsWith("https://jar") && !newLocation.contains("com.thomasjwilde.pdf")){
                setActualURL(newLocation);
            }

        });
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                if(webEngine.getLocation().endsWith("web/viewer.html")){
                    navigateToLocalPDF(localPDFPath);
                }
            }
        });
    }

    /**
     * Get the String URL location of the pdf.js viewer to load into the WebView
     * @return String of the pdf.js viewer.html url
     */
    public String getPDFJSViewer(){
//        URL url = getClass().getResource("/com/thomasjwilde/pdf/web/viewer.html");
        URL url = getClass().getResource("web/viewer.html");
        try {
            if (url != null) {
                return url.toURI().toString();
            }
        } catch (URISyntaxException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        log.error("Returning null for the viewer, resourse was not located");
        return null;
    }

    /**
     * Method to tell the pdf.js to load a local pdf path into the pdf.js viewer
     * @param path The local path of the document to be loaded
     */
    public void navigateToLocalPDF(Path path){
        byte[] fileContent = new byte[0];
        try {
            fileContent = Files.readAllBytes(path);
            String base64 = Base64.getEncoder().encodeToString(fileContent);
            webView.getEngine().executeScript("openFileFromBase64('" + base64 + "')");
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Method to download pdfs from online sources
     * @param fileURL The url of the pdf to download
     * @param targetPath The local target path to copy the pdf to
     */
    public void copyFileToTarget(String fileURL, Path targetPath){
        InputStream in = null;
        try {
            in = new URL(fileURL).openStream();
            Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } finally{
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    public WebView getWebView() {
        return webView;
    }

    public String getActualURL() {
        return actualURL.get();
    }

    public StringProperty actualURLProperty() {
        return actualURL;
    }

    public void setActualURL(String actualURL) {
        this.actualURL.set(actualURL);
    }
}
