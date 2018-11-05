package de.feu.propra18;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

/**
 * JavaFX Main Class, verwaltet das Fenster. Das eigentliche programm laeuft in {@link GodClass}.
 */
public class Main extends Application {

    public static void main(String[] args) {
            launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Konvexe Hülle");

        Parent root = FXMLLoader.load(getClass().getResource("/app.fxml"));

        Scene scene = new Scene(root, 400, 300);

        primaryStage.setScene(scene);
        primaryStage.show();

        Platform.setImplicitExit(false);
        primaryStage.setOnCloseRequest(event -> {
            showCloseDialog(primaryStage.getOwner());
            event.consume();
        });
    }

    /**
     * Oeffnet einen "wirklich schliessen?"-Dialog
     *
     * @param window
     */
    private void showCloseDialog(Window window){
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(window);

        Text text = new Text("Wollen sie die Anwendung wirklich schliessen?"
//                + "Nicht gespeicherte Punkte gehen verloren!"
        );
        Button yes = new Button("Ja");
        Button no = new Button("Nein");
        yes.setOnAction(event -> Platform.exit());
        no.setOnAction(event -> dialog.close());
        Pane yesno = new HBox(yes, no);
        Pane container= new VBox(text, yesno);
        Scene dialogScene = new Scene(container);
        dialog.setScene(dialogScene);
        dialog.show();
    }

}
