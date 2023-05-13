module org.alexandre.anonymisateur {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens org.alexandre.anonymisateur to javafx.fxml;
    exports org.alexandre.anonymisateur;
}