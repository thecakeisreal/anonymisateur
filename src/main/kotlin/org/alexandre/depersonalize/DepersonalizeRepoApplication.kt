package org.alexandre.depersonalize

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import org.kordamp.bootstrapfx.BootstrapFX

/**
 * Lance le système en affichant l'interface principale.
 */
class DepersonalizeRepoApplication : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(DepersonalizeRepoController::class.java.getResource("depersonalize_repo_view.fxml"))
        val scene = Scene(fxmlLoader.load(), 800.0, 500.0)

        scene.stylesheets.add(BootstrapFX.bootstrapFXStylesheet())
        stage.title = "Système de dépersonnalisation"
        stage.scene = scene
        stage.show()
    }
}

fun main() {
    Application.launch(DepersonalizeRepoApplication::class.java)
}