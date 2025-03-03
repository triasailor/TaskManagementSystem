module com.myreminder.myreminder {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires json.simple;

    opens com.myreminder.myreminder to javafx.fxml;
    exports com.myreminder.myreminder;
}