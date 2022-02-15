import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

/**
 * Zeigt das Hauptfenster mit Auswahlmenü an.
 *
 * 
 */
public class HauptseiteController implements Initializable {

    private Stage stage;

    private App mainApp;

    
    
    private static final String CONFIRMATION_TITLE = "Programm beenden";
    private static final String INFORMATION_TITLE = "Info";

    private static final String CONFIRMATION_TEXT = "Sollen alle Fenster geschlossen und die Anwendung beendet werden?";
    private static final String INFORMATION_TEXT = "Entwickler: \nAlexander Dünne";

    @FXML
    private void neuerUserMenuItemAction(ActionEvent event) {

        mainApp.zeigeNeuerUserDialog();
    }

    @FXML
    private void empfehlungUebersichtMenuItemAction(ActionEvent event) {
        mainApp.zeigeEmpfehlungUebersichtDialog();
    }

        @FXML
    private void profiltMenuItemAction(ActionEvent event) {
        mainApp.zeigeProfilDialog();
    }



    @FXML
    public void beendenMenuItemAction() {

        Alert meldung = new Alert(Alert.AlertType.CONFIRMATION, CONFIRMATION_TEXT, ButtonType.YES, ButtonType.NO);

        /* "Entfernt" den Header und setzt das Symbol links vom Inhalt. */
        meldung.setHeaderText("");
        meldung.setTitle(CONFIRMATION_TITLE);
        Optional<ButtonType> antwort = meldung.showAndWait(); // Programm wartet vor naechster Zeile
        if (antwort.isPresent()) {
            if (antwort.get().equals(ButtonType.YES)) {
                /* Anwendung schliessen. */
                Platform.exit();
            }
        }
    }

    @FXML
    private void infoButtonItemAction() {
        Alert meldung = new Alert(Alert.AlertType.INFORMATION);
//        Image image = new Image("/ok.png");
//        ImageView imageScale = new ImageView(image);
//        imageScale.setFitHeight(20);
//        imageScale.setFitWidth(20);
//        meldung.setGraphic(imageScale);
        meldung.setHeaderText("");
        meldung.setTitle(INFORMATION_TITLE);
        meldung.setContentText(INFORMATION_TEXT);
        meldung.showAndWait();
    }

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}