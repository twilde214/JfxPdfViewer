package com.thomasjwilde.demos.pdf;

import com.thomasjwilde.pdf.WebViewPDFJS;
import javafx.scene.web.WebView;

public class FullWebViewPDF extends DecoratedWebView {

    private WebViewPDFJS webViewPDFJS;
    public FullWebViewPDF(){
        webViewPDFJS = new WebViewPDFJS();
        setCenter(webViewPDFJS.getWebView());

        webViewPDFJS.actualURLProperty().addListener(((observable, oldValue, newValue) -> {
            getTextField().setText(newValue);
        }));


    }

    @Override
    public void goBack() {
        webViewPDFJS.getWebView().getEngine().executeScript("history.back()");
    }

    @Override
    public void goForward() {
        webViewPDFJS.getWebView().getEngine().executeScript("history.forward()");
    }

    @Override
    public void loadURL(String text) {
        webViewPDFJS.getWebView().getEngine().load(text);
    }

    public WebViewPDFJS getWebViewPDFJS() {
        return webViewPDFJS;
    }

    public WebView getWebView(){
        return webViewPDFJS.getWebView();
    }
}
