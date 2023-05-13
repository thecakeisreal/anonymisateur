package org.alexandre.anonymisateur

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.control.cell.PropertyValueFactory
import javafx.stage.DirectoryChooser
import javafx.stage.Stage
import java.io.File
import java.io.IOException
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.*
import kotlin.io.path.isDirectory
import kotlin.io.path.name

class AnonymRepoController {

    @FXML
    private lateinit var chosenDirTextField: TextField

    @FXML
    private lateinit var dirToRenameCountLabel: Label

    @FXML
    private lateinit var renamingInfoTable: TableView<Pair<String, String>>

    @FXML
    private lateinit var originalNameColumn: TableColumn<Pair<String, String>, String>

    @FXML
    private lateinit var renamedNameColumn: TableColumn<Pair<String, String>, String>

    private val renamingInfo: ObservableList<Pair<String, String>> = FXCollections.observableArrayList()

    @FXML
    private fun initialize() {
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

    @FXML
    private fun onPickDirClicked() {
        val chooserStage = Stage()
        chooserStage.isAlwaysOnTop = true

        val directoryChooser = DirectoryChooser()
        directoryChooser.initialDirectory = File("I:\\Documents\\Google Drive\\Cours - Cégep Victo")
        val selectedDirectory = directoryChooser.showDialog(chooserStage)

        chosenDirTextField.text = if (selectedDirectory == null) {
             ""
        } else {
           selectedDirectory.absolutePath
        }
    }

    private fun countValidDir(directoryPath: String): Int {
        return File(directoryPath).listFiles { f -> f.isDirectory }?.size ?: 0
    }

    private fun updateRenameInfo(directoryPath: String) {
        renamingInfo.clear()

        File(directoryPath).listFiles { f -> f.isDirectory }?.forEach {
            renamingInfo.add(Pair(it.name, UUID.randomUUID().toString().substring(0, 6)))
        }
    }

    @FXML
    private fun renameAllDirectories() {
        if(renamingInfo.isEmpty()) {
            return
        }

        val renameKeys = StringBuilder()
        val basePath = chosenDirTextField.text
        renamingInfo.forEach {
            renameKeys.appendLine("${it.first} : ${it.second}")
        }

        Files.write(Paths.get(basePath, "cles de conversion.txt"), renameKeys.lines(),
            StandardOpenOption.CREATE_NEW, StandardOpenOption.TRUNCATE_EXISTING)

        renamingInfo.forEach {
            Paths.get(basePath, it.first).toFile().renameTo(Paths.get(basePath, it.second).toFile())
        }
    }
}