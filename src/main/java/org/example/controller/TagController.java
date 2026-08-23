package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import org.example.entity.CalendarEntry;
import org.example.entity.User;
import org.example.service.CalendarEntryService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class TagController {

    @FXML
    private Label datumLabel;

    @FXML
    private ToggleButton jaButton;

    @FXML
    private ToggleButton neinButton;

    @FXML
    private ChoiceBox<String> muskelgruppeChoiceBox;

    @FXML
    private TextField kalorienField;

    @FXML
    private TextField fettField;

    @FXML
    private TextField kohlenhydrateField;

    private final CalendarEntryService calendarEntryService = new CalendarEntryService();

    private User user;
    private LocalDate datum;

    @FXML
    private void initialize() {
        ToggleGroup trainiertGruppe = new ToggleGroup();
        jaButton.setToggleGroup(trainiertGruppe);
        neinButton.setToggleGroup(trainiertGruppe);

        muskelgruppeChoiceBox.getItems().addAll(
                "Brust", "Rücken", "Beine", "Schultern", "Arme", "Bauch", "Ganzkörper");

        kalorienField.setTextFormatter(new TextFormatter<>(this::nurGanzeZahlen));
        fettField.setTextFormatter(new TextFormatter<>(this::nurKommazahl));
        kohlenhydrateField.setTextFormatter(new TextFormatter<>(this::nurKommazahl));
    }

    // wird von CalendarController nach dem Laden aufgerufen, um Tag und ggf. vorhandene Daten zu übergeben
    public void init(User user, LocalDate datum, CalendarEntry vorhandenerEintrag) {
        this.user = user;
        this.datum = datum;

        String wochentag = datum.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.GERMAN);
        datumLabel.setText(wochentag + ", " + datum.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        if (vorhandenerEintrag != null) {
            if (vorhandenerEintrag.isTrainiert()) {
                jaButton.setSelected(true);
            } else {
                neinButton.setSelected(true);
            }
            muskelgruppeChoiceBox.setValue(vorhandenerEintrag.getMuskelgruppe());
            kalorienField.setText(String.valueOf(vorhandenerEintrag.getKalorien()));
            fettField.setText(String.valueOf(vorhandenerEintrag.getFett()).replace('.', ','));
            kohlenhydrateField.setText(String.valueOf(vorhandenerEintrag.getKohlenhydrate()).replace('.', ','));
        }
    }

    private TextFormatter.Change nurGanzeZahlen(TextFormatter.Change change) {
        if (!change.getControlNewText().matches("\\d{0,5}")) {
            return null;
        }
        return change;
    }

    private TextFormatter.Change nurKommazahl(TextFormatter.Change change) {
        if (!change.getControlNewText().matches("\\d{0,4}(,\\d{0,2})?")) {
            return null;
        }
        return change;
    }

    @FXML
    private void onSpeichern() {
        if (!jaButton.isSelected() && !neinButton.isSelected()) {
            zeigeFehler("Bitte angeben, ob du trainiert hast.");
            return;
        }

        boolean trainiert = jaButton.isSelected();

        if (trainiert && muskelgruppeChoiceBox.getValue() == null) {
            zeigeFehler("Bitte eine Muskelgruppe auswählen.");
            return;
        }

        int kalorien = kalorienField.getText().isBlank() ? 0 : Integer.parseInt(kalorienField.getText());
        double fett = kalorienStringZuDouble(fettField.getText());
        double kohlenhydrate = kalorienStringZuDouble(kohlenhydrateField.getText());
        String muskelgruppe = trainiert ? muskelgruppeChoiceBox.getValue() : null;

        calendarEntryService.speichereEintrag(datum, trainiert, muskelgruppe, kalorien, fett, kohlenhydrate, user);

        ((Stage) datumLabel.getScene().getWindow()).close();
    }

    private double kalorienStringZuDouble(String text) {
        if (text == null || text.isBlank()) {
            return 0;
        }
        return Double.parseDouble(text.replace(',', '.'));
    }

    private void zeigeFehler(String text) {
        Alert alert = new Alert(Alert.AlertType.WARNING, text);
        alert.showAndWait();
    }
}
