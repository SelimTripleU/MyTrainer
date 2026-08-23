package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.App;
import org.example.entity.CalendarEntry;
import org.example.entity.User;
import org.example.service.CalendarEntryService;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CalendarController {

    @FXML
    private Label monatLabel;

    @FXML
    private GridPane tageGridPane;

    private final CalendarEntryService calendarEntryService = new CalendarEntryService();

    private User user;
    private YearMonth aktuellerMonat;

    public void init(User user) {
        this.user = user;
        this.aktuellerMonat = YearMonth.now();
        zeichneMonat();
    }

    // wird von PrimaryController beim Reset aufgerufen, damit der Kalender die gelöschten Einträge nicht mehr anzeigt
    public void aktualisiere() {
        zeichneMonat();
    }

    @FXML
    private void onVorherigerMonat() {
        aktuellerMonat = aktuellerMonat.minusMonths(1);
        zeichneMonat();
    }

    @FXML
    private void onNaechsterMonat() {
        aktuellerMonat = aktuellerMonat.plusMonths(1);
        zeichneMonat();
    }

    private void zeichneMonat() {
        String monatsName = aktuellerMonat.getMonth().getDisplayName(TextStyle.FULL, Locale.GERMAN);
        monatLabel.setText(monatsName.substring(0, 1).toUpperCase() + monatsName.substring(1) + " " + aktuellerMonat.getYear());

        tageGridPane.getChildren().clear();

        Map<LocalDate, CalendarEntry> eintraegeNachDatum = new HashMap<>();
        for (CalendarEntry eintrag : calendarEntryService.findeEintraegeFuerUser(user)) {
            eintraegeNachDatum.put(eintrag.getDatum(), eintrag);
        }

        LocalDate ersterTagDesMonats = aktuellerMonat.atDay(1);
        int spaltenOffset = ersterTagDesMonats.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();

        for (int tag = 1; tag <= aktuellerMonat.lengthOfMonth(); tag++) {
            LocalDate datum = aktuellerMonat.atDay(tag);
            int position = spaltenOffset + tag - 1;
            int spalte = position % 7;
            int zeile = position / 7;

            Button tagButton = new Button(String.valueOf(tag));
            tagButton.setPrefSize(48, 36);
            tagButton.setMaxWidth(Double.MAX_VALUE);

            CalendarEntry eintrag = eintraegeNachDatum.get(datum);
            if (eintrag != null) {
                tagButton.setStyle(eintrag.isTrainiert()
                        ? "-fx-background-color: #66bb6a;"
                        : "-fx-background-color: #ef5350;");
            }

            tagButton.setOnAction(e -> oeffneTagDialog(datum));

            tageGridPane.add(tagButton, spalte, zeile);
        }
    }

    private void oeffneTagDialog(LocalDate datum) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("tag.fxml"));
            Parent root = fxmlLoader.load();

            TagController tagController = fxmlLoader.getController();
            tagController.init(user, datum, calendarEntryService.findeEintrag(user, datum));

            Stage tagStage = new Stage();
            tagStage.setTitle("Tag bearbeiten");
            tagStage.initModality(Modality.APPLICATION_MODAL);
            tagStage.setScene(new Scene(root));
            tagStage.showAndWait();

            zeichneMonat();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
