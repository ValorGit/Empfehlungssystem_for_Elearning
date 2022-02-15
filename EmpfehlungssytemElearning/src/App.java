import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class App extends Application {
    private Stage primaryStage;

     
    /**
     * Öffnet das Fenster "Empfehlung"
     */
    public void zeigeEmpfehlungUebersichtDialog() {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("EmpfehlungUebersicht.fxml"));
            VBox EmpfehlungUebersichtView = (VBox) loader.load();
            Stage EmpfehlungUebersichtStage = new Stage();
            EmpfehlungUebersichtController controller = loader.getController();
            controller.setStage(EmpfehlungUebersichtStage);
            EmpfehlungUebersichtStage.setTitle("Empfehlungen");
            EmpfehlungUebersichtStage.initOwner(primaryStage);
            EmpfehlungUebersichtStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(EmpfehlungUebersichtView);
            EmpfehlungUebersichtStage.setScene(scene);
            EmpfehlungUebersichtStage.setOnCloseRequest((event) -> {
                controller.handleCloseButtonAction();
                event.consume();
            });

            EmpfehlungUebersichtStage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void zeigeProfilDialog() {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("Profil.fxml"));
            VBox ProfilView = (VBox) loader.load();
            Stage ProfilStage = new Stage();
            ProfilController controller = loader.getController();
            controller.setStage(ProfilStage);
            ProfilStage.setTitle("Profil");
            ProfilStage.initOwner(primaryStage);
            ProfilStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(ProfilView);
            ProfilStage.setScene(scene);
            ProfilStage.setOnCloseRequest((event) -> {
                controller.handleCloseButtonAction();
                event.consume();
            });

            ProfilStage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Öffnet das Fenster "Neuer User"
     */
    public void zeigeNeuerUserDialog() {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader
                    .setLocation(App.class
                            .getResource("neuerUser.fxml"));
            VBox neuerUserView = (VBox) loader.load();
            Stage neuerUserStage = new Stage();
            neuerUserController controller = loader.getController();
            controller.setStage(neuerUserStage);
            neuerUserStage.setTitle("Neuer User");
            neuerUserStage.initOwner(primaryStage);
            neuerUserStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(neuerUserView);
            neuerUserStage.setScene(scene);
            neuerUserStage.setOnCloseRequest((event) -> {
                controller.handleCloseButtonAction();
                event.consume();
            });
            neuerUserStage.showAndWait();

        } catch (IOException ex) {
            Logger.getLogger(App.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Öffnet das Fenster "Kunden anlegen"
     */
    public void zeigeHauptseite() {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader
                    .setLocation(App.class
                            .getResource("Hauptseite.fxml"));
            Parent root = loader.load();
            Stage HauptseiteStage = new Stage();
            HauptseiteController controller = loader.getController();
            controller.setStage(HauptseiteStage);
            controller.setMainApp(this);
            this.primaryStage.close();
            this.setPrimaryStage(HauptseiteStage);
            // this.primaryStage = HauptseiteStage;
            HauptseiteStage.setTitle("Hauptseite");
            // HauptseiteStage.initOwner(primaryStage);
            HauptseiteStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root);
            HauptseiteStage.setScene(scene);
            HauptseiteStage.setOnCloseRequest((event) -> {
                controller.handleCloseButtonAction();
                event.consume();
            });

            HauptseiteStage.show();

        } catch (IOException ex) {
            Logger.getLogger(App.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setPrimaryStage(Stage stage){
        this.primaryStage = stage;
    }

 
       @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Startseite");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(App.class.getResource("startseite.fxml"));
        Parent root = loader.load();
        Stage StartseiteStage = new Stage();

        startseiteController controller = loader.getController();
        controller.setMainApp(this);
        controller.setStage(StartseiteStage);

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        // primaryStage.setOnCloseRequest((event) -> {
        //         // controller.beendenMenuItemAction();
        //         event.consume();
        //     });
        primaryStage.show();
    }

    

    public static void main(String[] args) {
        launch();
    }

}