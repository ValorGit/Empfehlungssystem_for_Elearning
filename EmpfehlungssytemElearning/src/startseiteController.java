import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import javafx.application.Application;



public class startseiteController {

    @FXML
    private TextField benutzerTf;

    @FXML
    private Label errorLabel;

    @FXML
    private Button weiterBtn;

    private App mainApp;

    private Stage stage;

    public void setMainApp(App mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void handleCloseButtonAction() {

        stage.close();
       
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    

    public void weiterButtonPushed(){
        String lerner = benutzerTf.getText();
        Preferences pref = Preferences.userRoot();
            String userName = benutzerTf.getText();
            pref.put("username", userName);

            

        try (RecommenderEngine neuEngine = new RecommenderEngine()) {
            neuEngine.findeLerner(lerner);
            mainApp.zeigeHauptseite();
        //             try {
        //     FXMLLoader loader = new FXMLLoader();
        //     loader.setLocation(getClass().getResource("Hauptseite.FXML"));
                    
        //     VBox HauptseiteView = (VBox) loader.load();
        //     Stage HauptseiteStage = new Stage();
        //     mainApp.setPrimaryStage(HauptseiteStage);
        //     HauptseiteController controller = new HauptseiteController();
        //     // controller.initData("Mach doch einfach");
        //     controller = loader.getController();
        //     controller.setMainApp(mainApp);
        //     controller.setStage(HauptseiteStage);
        //     HauptseiteStage.setTitle("Hauptseite");
        //     // HauptseiteStage.initOwner(this);
        //     HauptseiteStage.initModality(Modality.WINDOW_MODAL);

        //     Scene scene = new Scene(HauptseiteView);
        //     HauptseiteStage.setScene(scene);
        //     // HauptseiteStage.setOnCloseRequest((event) -> {
        //     //     controller.handleCloseButtonAction();
        //     //     event.consume();
        //     // });
        //     stage.close();    
        //     HauptseiteStage.show();
        //     stage.close();

        // } catch (IOException ex) {
        //     Logger.getLogger(App.class
        //             .getName()).log(Level.SEVERE, null, ex);
        // }
    
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            benutzerTf.clear();
            errorLabel.setVisible(true);
        }
    }
}


    

