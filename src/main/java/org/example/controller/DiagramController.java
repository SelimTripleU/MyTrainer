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

public class DiagramController {

    @FXML
    private LineChart<String, Number> weightChart;

    @FXML
    private Label weightEmptyHintLabel;

    @FXML
    private BarChart<String, Number> trainingChart;

    @FXML
    private Label trainingEmptyHintLabel;

    private final BodyMeasurementService bodyMeasurementService = new BodyMeasurementService();
    private final CalendarEntryService calendarEntryService = new CalendarEntryService();

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yy");
    private static final DateTimeFormatter MONTH_FORMAT = DateTimeFormatter.ofPattern("MMM yy", Locale.GERMAN);

    public void init(User user) {
        drawWeightHistory(user);
        drawTrainingFrequency(user);
    }

    private void drawWeightHistory(User user) {
        List<BodyMeasurement> measurements = bodyMeasurementService.findAllByUser(user);

        if (measurements.isEmpty()) {
            weightChart.setVisible(false);
            weightChart.setManaged(false);
            weightEmptyHintLabel.setVisible(true);
            weightEmptyHintLabel.setManaged(true);
            return;
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Gewicht");

        for (BodyMeasurement measurement : measurements) {
            series.getData().add(new XYChart.Data<>(measurement.getDate().format(DATE_FORMAT), measurement.getWeight()));
        }

        weightChart.getData().add(series);
    }

    private void drawTrainingFrequency(User user) {
        List<CalendarEntry> entries = calendarEntryService.findEntriesForUser(user);

        if (entries.isEmpty()) {
            trainingChart.setVisible(false);
            trainingChart.setManaged(false);
            trainingEmptyHintLabel.setVisible(true);
            trainingEmptyHintLabel.setManaged(true);
            return;
        }

        // zählt pro Monat, an wie vielen Tagen trainiert bzw. nicht trainiert wurde; Monate ohne jeden Eintrag werden nicht gezeigt
        Map<YearMonth, Long> trainedDaysPerMonth = new TreeMap<>();
        Map<YearMonth, Long> untrainedDaysPerMonth = new TreeMap<>();
        for (CalendarEntry entry : entries) {
            YearMonth month = YearMonth.from(entry.getDate());
            trainedDaysPerMonth.putIfAbsent(month, 0L);
            untrainedDaysPerMonth.putIfAbsent(month, 0L);

            if (entry.isTrained()) {
                trainedDaysPerMonth.merge(month, 1L, Long::sum);
            } else {
                untrainedDaysPerMonth.merge(month, 1L, Long::sum);
            }
        }

        XYChart.Series<String, Number> trainedSeries = new XYChart.Series<>();
        trainedSeries.setName("Trainiert");

        XYChart.Series<String, Number> untrainedSeries = new XYChart.Series<>();
        untrainedSeries.setName("Nicht trainiert");

        for (Map.Entry<YearMonth, Long> entry : trainedDaysPerMonth.entrySet()) {
            String monthLabel = entry.getKey().atDay(1).format(MONTH_FORMAT);
            trainedSeries.getData().add(new XYChart.Data<>(monthLabel, entry.getValue()));
            untrainedSeries.getData().add(new XYChart.Data<>(monthLabel, untrainedDaysPerMonth.get(entry.getKey())));
        }

        trainingChart.getData().addAll(trainedSeries, untrainedSeries);
    }
}
