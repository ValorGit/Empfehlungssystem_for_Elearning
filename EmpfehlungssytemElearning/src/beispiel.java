// package aufgabe02;

// import Filter.FilterBestellNummer;
// import java.net.URL;
// import java.time.LocalDate;
// import java.util.Calendar;
// import java.util.GregorianCalendar;
// import java.util.ResourceBundle;
// import java.util.stream.Collectors;
// import javafx.beans.binding.Bindings;
// import javafx.beans.binding.DoubleBinding;
// import javafx.collections.FXCollections;
// import javafx.collections.ObservableList;
// import javafx.event.ActionEvent;
// import javafx.fxml.FXML;
// import javafx.fxml.Initializable;
// import javafx.scene.control.Alert;
// import javafx.scene.control.Button;
// import javafx.scene.control.ButtonType;
// import javafx.scene.control.DatePicker;
// import javafx.scene.control.Label;
// import javafx.scene.control.MenuItem;
// import javafx.scene.control.SelectionMode;
// import javafx.scene.control.TableColumn;
// import javafx.scene.control.TableColumn.CellEditEvent;
// import javafx.scene.control.TableView;
// import javafx.scene.control.TextField;
// import javafx.scene.control.TextFormatter;
// import javafx.scene.control.ToggleButton;
// import javafx.scene.control.cell.PropertyValueFactory;
// import javafx.scene.control.cell.TextFieldTableCell;
// import javafx.stage.Stage;
// import javafx.util.converter.IntegerStringConverter;

// /**
//  * Zeigt das Fenster zum Anlegen neuer Aufträge an.
//  *
//  * @author Alexander Dünne, Jürgen Christl
//  */
// public class AuftragAnlegenController implements Initializable {

//     // Variablen deklarieren
//     private MainApp mainApp;

//     private static final String FEHLER_KEIN_KUNDE = "Bitte geben Sie einen Auftraggeber ein.";

//     private static final String FEHLER_KEINE_BESTELLNUMMER = "Bitte geben Sie eine Bestellnummer ein.";

//     private static final String FEHLER_KEINE_GÜLTIGE_BESTELLNUMMER = "Bitte geben Sie eine gültige Bestellnummer ein.";

//     private static final String FEHLER_DATUM = "Der eingebene Wert liegt in der Vergangenheit.";

//     private static final String INFORMATION_TEXT = "Bitte füllen Sie die mit * markierten Pflichtfelder aus.";

//     @FXML
//     private Button abbrechenBtn;

//     @FXML
//     private Button speichernBtn;

//     @FXML
//     private MenuItem ProzessMenuItem;

//     @FXML
//     private DatePicker BestelldatumDp;

//     @FXML
//     private TextField auftragsIdTextField;

//     @FXML
//     private TextField kundeTF;

//     @FXML
//     private TextField bestellNrTF;

//     @FXML
//     private String auftragsId = setzeAuftragsId();

//     @FXML
//     private TableView ArtikelTable;

//     @FXML
//     private ToggleButton artikelBearbeitenButton;

//     @FXML
//     private Button LöschenButton;

//     @FXML
//     private TableView<Auftragsposition> artikelTableView;

//     @FXML
//     private TableColumn<Auftragsposition, String> artikelIdColumn;

//     @FXML
//     private TableColumn<Auftragsposition, String> bezeichnungColumn;

//     @FXML
//     private TableColumn<Auftragsposition, Integer> anzahlColumn;

//     @FXML
//     private TableColumn<Auftragsposition, Double> einzelpreisColumn;

//     @FXML
//     private TableColumn<Auftragsposition, Double> gesamtpreisColumn;

//     @FXML
//     private TextField artikelIdTextField;

//     @FXML
//     private TextField bezeichnungTextField;

//     @FXML
//     private TextField anzahlTextField;

//     @FXML
//     private TextField einzelpreisTextField;

//     @FXML
//     private TextField auftragswertTextField;

//     @FXML
//     private Label kundeMeldung;

//     @FXML
//     private Label bestellnrMeldung;

//     @FXML
//     private Label datumMeldung;

//     private int aId = 0;

//     private final Meldung meldung = new Meldung();

//     private Stage stage;

//     /**
//      * Weist dem Controller die Stage aus der Mainapp zu
//      *
//      * @param stage legt die Stage fest
//      */
//     public void setStage(Stage stage) {
//         this.stage = stage;
//     }

//     /**
//      * Aktualisiert Änderungen in der Spalte "Artikel-ID"
//      *
//      * @param edittedCell ist die editierte Zelle
//      */
//     public void changeArtikelIdCellEvent(CellEditEvent edittedCell) {
//         Auftragsposition positionSelected = artikelTableView.getSelectionModel().getSelectedItem();
//         positionSelected.setArtikelID(edittedCell.getNewValue().toString());

//     }

//     /**
//      * Aktualisiert Änderungen in der Spalte "Anzahl"
//      *
//      * @param edittedCell ist die editierte Zelle
//      */
//     public void changeAnzahlCellEvent(CellEditEvent edittedCell) {
//         Auftragsposition positionSelected = artikelTableView.getSelectionModel().getSelectedItem();
//         positionSelected.setAnzahl((int) edittedCell.getNewValue());

//         positionSelected.setGesamtpreis(positionSelected.getAnzahl() * positionSelected.getEinzelpreis());
//     }

//     /**
//      * Legt die Auftrags-ID fest
//      *
//      * @return gibt die AuftragsID aus.
//      */
//     private String setzeAuftragsId() {

//         aId = aId + 1;
//         Calendar cal = new GregorianCalendar();
//         return "AU" + cal.get(Calendar.YEAR) + aId;
//     }

//     @Override
//     public void initialize(URL url, ResourceBundle rb) {

//         //Wählt das heutige Datum als Standard beim Datepicker
//         BestelldatumDp.setValue(LocalDate.now());
//         bestellNrTF.setTextFormatter(new TextFormatter<>(new FilterBestellNummer()));

//         //Schreibt die Auftrags-ID in das Textfeld
//         auftragsIdTextField.textProperty().set(auftragsId);

//         //Legt die Tabellenspalten fest
//         artikelIdColumn.setCellValueFactory(new PropertyValueFactory<Auftragsposition, String>("ArtikelID"));
//         bezeichnungColumn.setCellValueFactory(new PropertyValueFactory<Auftragsposition, String>("Artikelbezeichnung"));
//         anzahlColumn.setCellValueFactory(new PropertyValueFactory<Auftragsposition, Integer>("Anzahl"));
//         einzelpreisColumn.setCellValueFactory(new PropertyValueFactory<Auftragsposition, Double>("Einzelpreis"));
//         gesamtpreisColumn.setCellValueFactory(new PropertyValueFactory<Auftragsposition, Double>("Gesamtpreis"));

//         //Läd die Beispieldaten
//         artikelTableView.setItems(getAuftragsposition());

//         //deaktiviert das Bearbeiten
//         artikelTableView.setEditable(false);

//         //Spalten editierbar machen
//         artikelIdColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//         bezeichnungColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//         anzahlColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

//         //Erlaubt das anwählen von mehreren Tabellenzeilen
//         artikelTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

//         //Berechnet den Auftragswert aus den Gesamtpreis der Tabelle
//         DoubleBinding total = Bindings.createDoubleBinding(()
//                 -> artikelTableView.getItems().stream().collect(Collectors.summingDouble(Auftragsposition::getGesamtpreis)),
//                 artikelTableView.getItems());

//         auftragswertTextField.textProperty().bind(Bindings.format("%.2f", total));

//         speichernBtn.setOnAction((ActionEvent event) -> {
//             boolean formIsValid = validateForm();
//             Alert meldung = new Alert(Alert.AlertType.INFORMATION, INFORMATION_TEXT, ButtonType.OK);

//             meldung.setHeaderText("");
// //            meldung.setTitle(CONFIRMATION_TITLE);

//             if (kundeTF.getText().isEmpty() || bestellNrTF.getText().isEmpty()) {

//                 meldung.showAndWait();
//             }
//             if (formIsValid) {
//                 speichern();
//                 stage.close();
//             }
//         });

//     }

//     /**
//      * Diese Methode löscht die ausgewählten Zeilen in der Tabelle
//      */
//     public void deleteButtonPushed() {
//         ObservableList<Auftragsposition> selectedRows, alleAuftragspos;
//         alleAuftragspos = artikelTableView.getItems();

//         selectedRows = artikelTableView.getSelectionModel().getSelectedItems();

//         for (Auftragsposition position : selectedRows) {
//             alleAuftragspos.remove(position);
//         }

//     }

//     //Wird für bearbeitenButtonPushed() benötigt
//     boolean bearbeiten = false;

//     /**
//      * Aktiviert das Bearbeiten der Tabelle, solange der Button aktiv ist
//      */
//     public void bearbeitenButtonPushed() {

//         bearbeiten = !bearbeiten;
//         artikelTableView.setEditable(bearbeiten);
//     }

//     /**
//      * Fügt eine neue Auftragsposition in der Tabelle ein
//      */
//     public void neueAuftragsposButtonPushed() {
//         Auftragsposition newPosition = new Auftragsposition(artikelIdTextField.getText(),
//                 bezeichnungTextField.getText(),
//                 Integer.valueOf(anzahlTextField.getText()),
//                 Double.valueOf(einzelpreisTextField.getText()),
//                 Integer.valueOf(anzahlTextField.getText())
//                 * Double.valueOf(einzelpreisTextField.getText()));

//         artikelTableView.getItems().add(newPosition);
//     }

//     /**
//      * Erstellung der Beispieldatensätze in der Tabelle
//      *
//      * @return gibt die Tabellenzeilen aus
//      */
//     public ObservableList<Auftragsposition> getAuftragsposition() {
//         ObservableList<Auftragsposition> position = FXCollections.observableArrayList();
//         position.add(new Auftragsposition("AR20181", "Uran", 2, 50.0, 100.0));

//         position.add(new Auftragsposition("AR20182", "Plutonium", 2, 80.0, 160.0));

//         return position;
//     }

//     /**
//      * Schließt das Fenster
//      *
//      * @param event wird bei Betätigung des Buttons ausgelöst
//      */
//     @FXML
//     public void handleCloseButtonAction() {

//         if ((kundeTF.getText() == null || kundeTF.getText().trim().isEmpty())
//                 && (bestellNrTF.getText() == null || bestellNrTF.getText().trim().isEmpty())) {
//             stage.close();
//         } else {
//             if (meldung.showExitPlatformAlert()) {
//                 stage.close();
//             }
//         }
//     }

//     private void speichern() {

//         System.out.println("Hab gespeichert");
//         // In Datenstruktur ablegen
// //        resetForm();
//     }

//     private boolean validateForm() {

//         boolean validate = true;

//         if (BestelldatumDp.getValue().isBefore(LocalDate.now())) {
//             datumMeldung.setText(FEHLER_DATUM);
//             BestelldatumDp.requestFocus();

//             validate = false;

//         } else {
//             datumMeldung.setText("");
//         }

//         if (kundeTF.getText().isEmpty()) {

//             kundeMeldung.setText(FEHLER_KEIN_KUNDE);
//             kundeTF.requestFocus();

//             validate = false;

//         } else {

//             kundeMeldung.setText("");
//         }

//         if (bestellNrTF.getText().isEmpty()) {

//             bestellnrMeldung.setText(FEHLER_KEINE_BESTELLNUMMER);
//             bestellNrTF.requestFocus();

//             validate = false;

//         } else {
//             if (!bestellNrTF.getText().matches("[0-9]{2}" + "/?" + "[0-9]{4}")) {

//                 bestellnrMeldung.setText(FEHLER_KEINE_GÜLTIGE_BESTELLNUMMER);
//                 bestellNrTF.requestFocus();

//                 validate = false;
//             } else {

//                 bestellnrMeldung.setText("");
//             }
//         }

//         return validate;
//     }

//     /**
//      * Legt das Fenster als aktives Fenster fest
//      *
//      * @param mainApp
//      */
//     public void setMainApp(MainApp mainApp) {
//         this.mainApp = mainApp;
//     }
// }
