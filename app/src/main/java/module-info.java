module com.thomasjwilde.demos.pdf {
    requires com.thomasjwilde.pdf;
    requires javafx.controls;
    requires javafx.web;
    requires javafx.graphics;

    exports com.thomasjwilde.demos.pdf to javafx.graphics;
}