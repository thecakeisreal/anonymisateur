module org.alexandre.depersonalize {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens org.alexandre.depersonalize to javafx.fxml;
    exports org.alexandre.depersonalize;
}