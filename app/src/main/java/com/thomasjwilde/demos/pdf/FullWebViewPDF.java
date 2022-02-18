package com.thomasjwilde.demos.pdf;

import com.thomasjwilde.pdf.WebViewPDFJS;

public class FullWebViewPDF extends DecoratedWebView {

    private WebViewPDFJS webViewPDFJS;
    public FullWebViewPDF(){
        webViewPDFJS = new WebViewPDFJS();
        setCenter(webViewPDFJS);
        webViewPDFJS.getWebView().getEngine().locationProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null && !newValue.startsWith("http://jar")){
                getTextField().setText(newValue);
            }
        });
        webViewPDFJS.pseudoURLProperty().addListener(((observable, oldValue, newValue) -> {
            getTextField().setText(newValue);
        }));
        webViewPDFJS.navigateToPdfViewer();


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
}
