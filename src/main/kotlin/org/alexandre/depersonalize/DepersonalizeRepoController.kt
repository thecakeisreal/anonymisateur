package org.alexandre.depersonalize

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.cell.PropertyValueFactory
import javafx.stage.DirectoryChooser
import javafx.stage.Stage
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.*

/**
 * Contrôleur de renommage de répertoire. Gère les opérations pour renommer l'ensemble des sous-répertoires
 * d'un répertoire en un ensemble dépersonnalisé
 */
class DepersonalizeRepoController {

    /**
     * Affiche le nom du répertoire sélectionné
     */
    @FXML
    private lateinit var chosenDirTextField: TextField

    /**
     * Affiche le nombre de répertoires à renommer
     */
    @FXML
    private lateinit var dirToRenameCountLabel: Label

    /**
     * Affiche comme le renommage sera fait
     */
    @FXML
    private lateinit var renamingInfoTable: TableView<Pair<String, String>>

    /**
     * Colonne affichant le nom actuel des éléments renommés
     */
    @FXML
    private lateinit var originalNameColumn: TableColumn<Pair<String, String>, String>

    /**
     * Colonne affichant le nouveau nom des éléments
     */
    @FXML
    private lateinit var renamedNameColumn: TableColumn<Pair<String, String>, String>

    /**
     * Liste des éléments à renommer
     */
    private val renamingInfo: ObservableList<Pair<String, String>> = FXCollections.observableArrayList()

    @FXML
    private fun initialize() {
        // Met à jour la table lors du changement du champ texte
        chosenDirTextField.textProperty().addListener { _, _, newValue ->
            val dirToRenameCount = countValidDir(newValue)
            dirToRenameCountLabel.text = "$dirToRenameCount répertoire à renommer."

            updateRenameInfo(newValue)
        }

        originalNameColumn.cellValueFactory = PropertyValueFactory("first")
        renamedNameColumn.cellValueFactory = PropertyValueFactory("second")

        renamingInfoTable.items = renamingInfo
        renamingInfoTable.columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY


    }

    /**
     * Permet de sélectionner un répertoire dans lequel réaliser la dépersonnalisation lorsque l'on clique
     * sur le bouton.
     */
    @FXML
    private fun onPickDirClicked() {
        val chooserStage = Stage()
        chooserStage.isAlwaysOnTop = true

        val directoryChooser = DirectoryChooser()
        directoryChooser.initialDirectory = File("I:\\Documents\\Google Drive\\Cours - Cégep Victo")
        val selectedDirectory = directoryChooser.showDialog(chooserStage)

        // Met à jour le champ texte
        if (selectedDirectory != null) {
            chosenDirTextField.text = selectedDirectory.absolutePath
        }
    }

    /**
     * Compte le nombre de répertoires à l'emplacement [directoryPath] à renommer.
     */
    private fun countValidDir(directoryPath: String): Int {
        return File(directoryPath).listFiles { f -> f.isDirectory }?.size ?: 0
    }

    /**
     * Met à jour le table et les informations de renommage à partir des répertoires enfants de [directoryPath].
     */
    private fun updateRenameInfo(directoryPath: String) {
        do {
            renamingInfo.clear()

            File(directoryPath).listFiles { f -> f.isDirectory }?.forEach {
                renamingInfo.add(Pair(it.name, UUID.randomUUID().toString().substring(0, 6)))
            }
            // Vérifie l'unicité des valeurs
        } while (renamingInfo.stream().map { p -> p.second }.distinct().count().toInt() != renamingInfo.size)
    }

    /**
     * Renomme les répertoires à partir du chemin sélectionné.
     */
    @FXML
    private fun renameAllDirectories() {
        if (renamingInfo.isEmpty()) {
            return
        }

        try {
            writeRenamingKeyFile()

            val basePath = chosenDirTextField.text
            renamingInfo.forEach {
                Paths.get(basePath, it.first).toFile().renameTo(Paths.get(basePath, it.second).toFile())
            }
        } catch (exception: IOException) {
            Alert(
                AlertType.ERROR, "Impossible d'écrire les clés de renommage\n ${exception.message}",
                ButtonType.OK
            )
        }
    }

    /**
     * Écrit le fichier qui contient les clés de renommages.
     */
    private fun writeRenamingKeyFile() {
        val renameKeys = StringBuilder()
        val basePath = chosenDirTextField.text
        renamingInfo.forEach {
            renameKeys.appendLine("${it.first} : ${it.second}")
        }

        Files.write(
            Paths.get(basePath, "cles de conversion.txt"), renameKeys.lines(),
            StandardOpenOption.CREATE_NEW, StandardOpenOption.TRUNCATE_EXISTING
        )
    }
}