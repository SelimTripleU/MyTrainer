package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import org.example.entity.BodyMeasurement;
import org.example.entity.CalendarEntry;
import org.example.entity.User;
import org.example.service.BodyMeasurementService;
import org.example.service.CalendarEntryService;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class DiagrammController {

    @FXML
    private LineChart<String, Number> gewichtChart;

    @FXML
    private Label gewichtLeerHinweisLabel;

    @FXML
    private BarChart<String, Number> trainingsChart;

    @FXML
    private Label trainingsLeerHinweisLabel;

    private final BodyMeasurementService bodyMeasurementService = new BodyMeasurementService();
    private final CalendarEntryService calendarEntryService = new CalendarEntryService();

    private static final DateTimeFormatter DATUM_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yy");
    private static final DateTimeFormatter MONAT_FORMAT = DateTimeFormatter.ofPattern("MMM yy", Locale.GERMAN);

    public void init(User user) {
        zeichneGewichtsverlauf(user);
        zeichneTrainingsfrequenz(user);
    }

    private void zeichneGewichtsverlauf(User user) {
        List<BodyMeasurement> messungen = bodyMeasurementService.findAllByUser(user);

        if (messungen.isEmpty()) {
            gewichtChart.setVisible(false);
            gewichtChart.setManaged(false);
            gewichtLeerHinweisLabel.setVisible(true);
            gewichtLeerHinweisLabel.setManaged(true);
            return;
        }

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Gewicht");

        for (BodyMeasurement messung : messungen) {
            serie.getData().add(new XYChart.Data<>(messung.getDatum().format(DATUM_FORMAT), messung.getWeight()));
        }

        gewichtChart.getData().add(serie);
    }

    private void zeichneTrainingsfrequenz(User user) {
        List<CalendarEntry> eintraege = calendarEntryService.findeEintraegeFuerUser(user);

        if (eintraege.isEmpty()) {
            trainingsChart.setVisible(false);
            trainingsChart.setManaged(false);
            trainingsLeerHinweisLabel.setVisible(true);
            trainingsLeerHinweisLabel.setManaged(true);
            return;
        }

        // zählt pro Monat, an wie vielen Tagen trainiert wurde; Monate ohne jeden Eintrag werden nicht gezeigt
        Map<YearMonth, Long> trainierteTageProMonat = new TreeMap<>();
        for (CalendarEntry eintrag : eintraege) {
            YearMonth monat = YearMonth.from(eintrag.getDatum());
            trainierteTageProMonat.putIfAbsent(monat, 0L);
            if (eintrag.isTrainiert()) {
                trainierteTageProMonat.merge(monat, 1L, Long::sum);
            }
        }

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Trainierte Tage");

        for (Map.Entry<YearMonth, Long> eintrag : trainierteTageProMonat.entrySet()) {
            String monatsLabel = eintrag.getKey().atDay(1).format(MONAT_FORMAT);
            serie.getData().add(new XYChart.Data<>(monatsLabel, eintrag.getValue()));
        }

        trainingsChart.getData().add(serie);
    }
}
