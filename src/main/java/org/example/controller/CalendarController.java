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
    private Label monthLabel;

    @FXML
    private GridPane daysGridPane;

    private final CalendarEntryService calendarEntryService = new CalendarEntryService();

    private User user;
    private YearMonth currentMonth;

    public void init(User user) {
        this.user = user;
        this.currentMonth = YearMonth.now();
        drawMonth();
    }

    // called by PrimaryController on reset, so the calendar no longer shows the deleted entries
    public void refresh() {
        drawMonth();
    }

    @FXML
    private void onPreviousMonth() {
        currentMonth = currentMonth.minusMonths(1);
        drawMonth();
    }

    @FXML
    private void onNextMonth() {
        currentMonth = currentMonth.plusMonths(1);
        drawMonth();
    }

    private void drawMonth() {
        String monthName = currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.GERMAN);
        monthLabel.setText(monthName.substring(0, 1).toUpperCase() + monthName.substring(1) + " " + currentMonth.getYear());

        daysGridPane.getChildren().clear();

        Map<LocalDate, CalendarEntry> entriesByDate = new HashMap<>();
        for (CalendarEntry entry : calendarEntryService.findEntriesForUser(user)) {
            entriesByDate.put(entry.getDate(), entry);
        }

        LocalDate firstDayOfMonth = currentMonth.atDay(1);
        int columnOffset = firstDayOfMonth.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();

        for (int day = 1; day <= currentMonth.lengthOfMonth(); day++) {
            LocalDate date = currentMonth.atDay(day);
            int position = columnOffset + day - 1;
            int column = position % 7;
            int row = position / 7;

            Button dayButton = new Button(String.valueOf(day));
            dayButton.getStyleClass().add("day-button");
            dayButton.setPrefSize(48, 36);
            dayButton.setMaxWidth(Double.MAX_VALUE);

            CalendarEntry entry = entriesByDate.get(date);
            if (entry != null) {
                dayButton.setStyle(entry.isTrained()
                        ? "-fx-background-color: #66bb6a;"
                        : "-fx-background-color: #ef5350;");
            }

            dayButton.setOnAction(e -> openDayDialog(date));

            daysGridPane.add(dayButton, column, row);
        }
    }

    private void openDayDialog(LocalDate date) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("tag.fxml"));
            Parent root = fxmlLoader.load();

            DayController dayController = fxmlLoader.getController();
            dayController.init(user, date, calendarEntryService.findEntry(user, date));

            Stage dayStage = new Stage();
            dayStage.setTitle("Tag bearbeiten");
            dayStage.initModality(Modality.APPLICATION_MODAL);
            dayStage.setScene(new Scene(root));
            dayStage.showAndWait();

            drawMonth();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
