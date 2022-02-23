module com.thomasjwilde.demos.pdf {
    requires com.thomasjwilde.pdf;
    requires javafx.controls;
    requires javafx.web;
    requires javafx.graphics;
    requires jdk.crypto.cryptoki;
    requires org.apache.logging.log4j;

    exports com.thomasjwilde.demos.pdf to javafx.graphics;
}