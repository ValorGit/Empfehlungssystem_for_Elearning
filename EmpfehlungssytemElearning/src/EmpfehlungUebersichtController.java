import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import org.neo4j.driver.Record;

public class EmpfehlungUebersichtController implements Initializable{

    private Stage stage;
    private App mainApp;

    private String currentuser = "test";

    @FXML
    private Label aktuellePositionLabel;

    @FXML
    private Label lernerLabel;

    @FXML
    private ListView<Kurs> listViewEmpfehlungAktuell;

    @FXML
    private ListView<Kurs> listViewEmpfehlungZiel;

    @FXML
    private Label zielPositionLabel;
    
    
    // public void initData(String user){
    //     currentuser = user;
    //     System.out.println(("Jetzt werde ich gestartet"));
    // }
   


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        currentuser = Preferences.userRoot().get("username", "User");
        lernerLabel.setText(currentuser);
        ObservableList<Kurs> kursObservableList = FXCollections.observableArrayList();
        
        try (RecommenderEngine re = new RecommenderEngine()) {
            re.findeLerner(currentuser);
            aktuellePositionLabel.setText(re.gibaktuellePosition(currentuser));
            List<Record> kurse = re.empfehleKurs(currentuser);
            
            // Node[] nodes = new Node[kurs.size()];
        for(int i = 0; i < kurse.size(); i++){
            Kurs kurs = new Kurs(kurse.get(i).values().get(0).get("name").asString());
            kursObservableList.add(kurs);
          
        }
       
        listViewEmpfehlungAktuell.setItems(kursObservableList);
        listViewEmpfehlungAktuell.setCellFactory(kursListView -> new KursListViewCell());


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(currentuser);
    }

    /**
     * Weist dem Controller die Stage aus der Mainapp zu
     *
     * @param stage legt die Stage fest
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void handleCloseButtonAction() {

        stage.close();
       
    }


    public void setMainApp(App mainApp) {
        this.mainApp = mainApp;
    }

    
}
