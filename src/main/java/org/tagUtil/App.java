package org.tagUtil;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.tagUtil.Util.AudioHelper;
import org.tagUtil.Util.FileHelper;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class App extends Application {

    public static Logger logger;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("primary.fxml"));
        Scene scene = new Scene(root, 600, 400);
        ScrollPane scrollPane = (ScrollPane) scene.lookup("#logPane");
        Text text = new Text();
        logger = new Logger(text);
        scrollPane.setContent(text);
        VBox dragTarget = (VBox) scene.lookup("#dragBox");
        dragTarget.setOnDragOver(event -> {
            if (event.getGestureSource() != dragTarget && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        dragTarget.setOnDragDropped(this::handle);
        primaryStage.setTitle("Folder Util");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handle(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            List<File> fileList = db.getFiles();

            Predicate<File> isJunk = file -> FilenameUtils.isExtension(file.getName().toLowerCase(), "nfo", "log", "cue", "m3u", "m3u8", "md5");
            fileList.stream().filter(isJunk).forEach(File::delete);
            fileList.removeIf(isJunk);

            for (File file : fileList) {
                if (file.isDirectory()) {
                    Iterator<File> iterator = FileUtils.iterateFiles(file, null, true);
                    while (iterator.hasNext()) {
                        handleFile(iterator.next());
                    }
                } else {
                    handleFile(file);
                }
            }
            success = true;
        }
        event.setDropCompleted(success);
        event.consume();
    }

    private void handleFile(File file) {
        if (file.isDirectory()) {
            Iterator<File> iterator = FileUtils.iterateFiles(file, null, true);
            while (iterator.hasNext()) {
                handleFile(iterator.next());
            }
        } else {
            if (FileHelper.isImage(file)) {
                FileHelper.handleImage(file);
            } else if (FileHelper.isAudio(file)) {
                AudioHelper.handleAudio(file);
            }
        }
    }
}