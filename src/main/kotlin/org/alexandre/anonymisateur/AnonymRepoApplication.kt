package org.alexandre.anonymisateur

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import org.kordamp.bootstrapfx.BootstrapFX

class AnonymRepoApplication : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(AnonymRepoApplication::class.java.getResource("anonyme_repo_view.fxml"))
        val scene = Scene(fxmlLoader.load(), 800.0, 500.0)

        scene.stylesheets.add(BootstrapFX.bootstrapFXStylesheet())
        stage.title = "Anonymisateur"
        stage.scene = scene
        stage.show()
    }
}

fun main() {
    Application.launch(AnonymRepoApplication::class.java)
}