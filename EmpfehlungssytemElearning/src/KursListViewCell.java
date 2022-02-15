
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;

/**
 * 
 *
 */

public class KursListViewCell extends ListCell<Kurs> {

    @FXML
    private Label kursTitel;

    private FXMLLoader mLLoader;

    @FXML
    private AnchorPane anchor;

    @Override
    protected void updateItem(Kurs kurs, boolean empty) {
        super.updateItem(kurs, empty);

        if(empty || kurs == null) {

            // setText(null);
            // setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("testItem.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            kursTitel.setText(kurs.getName());

            anchor.setOnMouseClicked(new EventHandler<MouseEvent>(){

          @Override
          public void handle(MouseEvent arg0) {
            
              System.out.println(kursTitel.getText());
          }

      });

            


            setText(null);
            setGraphic(anchor);

        }

    }

    public void gibText(){
        System.out.println(kursTitel.getText());

    }
}
