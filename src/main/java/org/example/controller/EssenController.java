package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.App;
import org.example.entity.ErnaehrungsZiel;
import org.example.entity.Rezept;
import org.example.entity.User;
import org.example.service.ErnaehrungsZielService;
import org.example.service.RezeptService;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class EssenController {

    @FXML
    private Label kalorienLabel;

    @FXML
    private Label proteinLabel;

    @FXML
    private Label fettLabel;

    @FXML
    private Label kohlenhydrateLabel;

    @FXML
    private ListView<String> rezepteListView;

    private final RezeptService rezeptService = new RezeptService();
    private final ErnaehrungsZielService ernaehrungsZielService = new ErnaehrungsZielService();

    private List<Rezept> aktuelleRezepte;

    @FXML
    private void initialize() {
        rezeptService.seedStandardRezepteFallsLeer();

        rezepteListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                zeigeRezept();
            }
        });
    }

    public void init(User user) {
        ErnaehrungsZiel ziel = ernaehrungsZielService.findZielFuerUser(user);

        if (ziel == null) {
            kalorienLabel.setText("Noch keine Werte berechnet.");
            proteinLabel.setText("Bitte zuerst im BMI-Rechner deine Werte berechnen.");
            fettLabel.setText("");
            kohlenhydrateLabel.setText("");

            aktuelleRezepte = rezeptService.findAllRezepte();
        } else {
            kalorienLabel.setText(String.format(Locale.GERMANY, "Kalorienziel: %d kcal/Tag", ziel.getKalorien()));
            proteinLabel.setText(String.format(Locale.GERMANY, "Protein: %.0f g/Tag", ziel.getProtein()));
            fettLabel.setText(String.format(Locale.GERMANY, "Fett: %.0f g/Tag", ziel.getFett()));
            kohlenhydrateLabel.setText(String.format(Locale.GERMANY, "Kohlenhydrate: %.0f g/Tag", ziel.getKohlenhydrate()));

            aktuelleRezepte = "Gewicht halten".equals(ziel.getZiel())
                    ? rezeptService.findAllRezepte()
                    : rezeptService.findRezepteByZiel(ziel.getZiel());
        }

        rezepteListView.getItems().clear();
        for (Rezept rezept : aktuelleRezepte) {
            rezepteListView.getItems().add(rezept.getName());
        }
    }

    private void zeigeRezept() {
        int ausgewaehlterIndex = rezepteListView.getSelectionModel().getSelectedIndex();
        if (ausgewaehlterIndex < 0) {
            return;
        }

        Rezept rezept = aktuelleRezepte.get(ausgewaehlterIndex);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("rezeptDetail.fxml"));
            Parent root = fxmlLoader.load();

            RezeptDetailController rezeptDetailController = fxmlLoader.getController();
            rezeptDetailController.init(rezept);

            Stage rezeptStage = new Stage();
            rezeptStage.setTitle(rezept.getName());
            rezeptStage.initModality(Modality.APPLICATION_MODAL);
            rezeptStage.setScene(new Scene(root));
            rezeptStage.show();
        } catch (IOException e) {
            zeigeFehler("Rezept konnte nicht geöffnet werden.");
        }
    }

    private void zeigeFehler(String text) {
        Alert alert = new Alert(Alert.AlertType.WARNING, text);
        alert.showAndWait();
    }
}
