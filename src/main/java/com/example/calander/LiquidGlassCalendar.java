package com.example.calander;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.PauseTransition;
import java.time.*;
import java.util.*;

public class LiquidGlassCalendar extends Application {
    static class Event {
        String title, note, category;
        int year, month, day;
        boolean recurring;
        LocalTime reminderTime;

        Event(String title, String note, String category, int year, int month, int day, boolean recurring, LocalTime reminderTime) {
            this.title = title; this.note = note; this.category = category;
            this.year = year; this.month = month; this.day = day;
            this.recurring = recurring;
            this.reminderTime = reminderTime;
        }
        String getColor() {
            switch (category) {
                case "Work": return "#2196F3";
                case "Study": return "#4CAF50";
                case "Health": return "#FF9800";
                case "Personal": return "#9C27B0";
                default: return "#9E9E9E";
            }
        }
    }

    private final List<Event> events = new ArrayList<>();
    private ComboBox<Integer> yearCombo;
    private ComboBox<String> monthCombo;
    private GridPane calendarGrid;
    private Pane glassPane;
    private ToggleButton darkModeToggle;
    private ComboBox<String> themePicker;
    private ComboBox<String> langPicker;
    private TextField searchBar;
    private VBox sidebarMenu;
    private boolean sidebarVisible = false;
    private ResourceBundle bundle;
    private String currentLang = "English";
    private Scene mainScene;

    private void setBundle() {
        if (currentLang.equals("বাংলা")) {
            bundle = new ListResourceBundle() {
                protected Object[][] getContents() {
                    return new Object[][] {
                            {"calendar_title", "Liquid Glass ক্যালেন্ডার"},
                            {"dark_mode", "ডার্ক মোড"},
                            {"search_events", "ইভেন্ট খুঁজুন..."},
                            {"mon", "সোম"}, {"tue", "মঙ্গল"}, {"wed", "বুধ"},
                            {"thu", "বৃহস্পতি"}, {"fri", "শুক্র"}, {"sat", "শনি"}, {"sun", "রবি"},
                            {"settings", "সেটিংস"},
                            {"add_event", "ইভেন্ট যোগ করুন"},
                            {"edit_event", "ইডিট"},
                            {"delete_event", "ডিলিট"},
                            {"event_title", "শিরোনাম"},
                            {"event_note", "নোট"},
                            {"category", "ক্যাটাগরি"},
                            {"close", "বন্ধ করুন"},
                            {"save", "সেভ"},
                            {"recurring", "পুনরাবৃত্ত"},
                            {"reminder", "রিমাইন্ডার"},
                            {"personal", "ব্যক্তিগত"},
                            {"work", "কাজ"},
                            {"study", "পড়া"},
                            {"health", "স্বাস্থ্য"},
                            {"other", "অন্যান্য"}
                    };
                }
            };
        } else {
            bundle = new ListResourceBundle() {
                protected Object[][] getContents() {
                    return new Object[][] {
                            {"calendar_title", "Liquid Glass Calendar"},
                            {"dark_mode", "Dark Mode"},
                            {"search_events", "Search events..."},
                            {"mon", "Mon"}, {"tue", "Tue"}, {"wed", "Wed"},
                            {"thu", "Thu"}, {"fri", "Fri"}, {"sat", "Sat"}, {"sun", "Sun"},
                            {"settings", "Settings"},
                            {"add_event", "Add Event"},
                            {"edit_event", "Edit"},
                            {"delete_event", "Delete"},
                            {"event_title", "Title"},
                            {"event_note", "Note"},
                            {"category", "Category"},
                            {"close", "Close"},
                            {"save", "Save"},
                            {"recurring", "Recurring"},
                            {"reminder", "Reminder"},
                            {"personal", "Personal"},
                            {"work", "Work"},
                            {"study", "Study"},
                            {"health", "Health"},
                            {"other", "Other"}
                    };
                }
            };
        }
    }

    @Override
    public void start(Stage primaryStage) {
        setBundle();
        yearCombo = new ComboBox<>();
        for (int y = 1990; y <= 2050; y++) yearCombo.getItems().add(y);
        yearCombo.setValue(LocalDate.now().getYear());

        monthCombo = new ComboBox<>();
        for (Month m : Month.values()) monthCombo.getItems().add(m.name());
        monthCombo.setValue(LocalDate.now().getMonth().name());

        themePicker = new ComboBox<>();
        themePicker.getItems().addAll("Light", "Dark", "Blue", "Green");
        themePicker.setValue("Light");

        darkModeToggle = new ToggleButton(bundle.getString("dark_mode"));

        langPicker = new ComboBox<>();
        langPicker.getItems().addAll("English", "বাংলা");
        langPicker.setValue(currentLang);

        searchBar = new TextField();
        searchBar.setPromptText(bundle.getString("search_events"));

        Button hamburger = new Button("☰");
        hamburger.setOnAction(e -> toggleSidebar());

        sidebarMenu = createSidebarMenu();

        HBox topBar = new HBox(10, hamburger, monthCombo, yearCombo, searchBar, themePicker, darkModeToggle, langPicker);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10));
        topBar.getStyleClass().add("top-bar-glass");

        calendarGrid = createCalendarGrid(YearMonth.of(yearCombo.getValue(), Month.valueOf(monthCombo.getValue())));

        glassPane = createGlassPane();
        glassPane.getChildren().addAll(topBar, calendarGrid, sidebarMenu);

        StackPane.setAlignment(glassPane, Pos.CENTER);
        StackPane root = new StackPane(glassPane);
        root.setPadding(new Insets(40));
        root.getStyleClass().add("background-image");

        mainScene = new Scene(root, 950, 700);
        mainScene.getStylesheets().add(getClass().getResource("calendar.css").toExternalForm());

        yearCombo.setOnAction(e -> updateCalendar());
        monthCombo.setOnAction(e -> updateCalendar());
        themePicker.setOnAction(e -> applyTheme(themePicker.getValue()));
        darkModeToggle.setOnAction(e -> applyTheme(darkModeToggle.isSelected() ? "Dark" : themePicker.getValue()));
        langPicker.setOnAction(e -> {
            currentLang = langPicker.getValue();
            primaryStage.close();
            start(new Stage());
        });
        searchBar.textProperty().addListener((obs, o, n) -> updateCalendar());

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                Platform.runLater(() -> checkReminders());
            }
        }, 0, 60 * 1000);

        primaryStage.setTitle(bundle.getString("calendar_title"));
        primaryStage.setScene(mainScene);
        applyTheme("Light");
        primaryStage.show();
    }

    private void updateCalendar() {
        int year = yearCombo.getValue();
        Month month = Month.valueOf(monthCombo.getValue());
        GridPane newGrid = createCalendarGrid(YearMonth.of(year, month));
        glassPane.getChildren().set(1, newGrid);
    }

    private Pane createGlassPane() {
        VBox pane = new VBox(20);
        pane.setAlignment(Pos.TOP_CENTER);
        pane.setPrefSize(900, 600);
        pane.setPadding(new Insets(25));
        pane.getStyleClass().add("glass-pane");
        return pane;
    }

    private GridPane createCalendarGrid(YearMonth yearMonth) {
        LocalDate today = LocalDate.now();
        LocalDate firstOfMonth = yearMonth.atDay(1);
        int firstDayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 10, 10, 10));
        grid.setHgap(12);
        grid.setVgap(12);

        String[] days = {bundle.getString("mon"), bundle.getString("tue"), bundle.getString("wed"),
                bundle.getString("thu"), bundle.getString("fri"), bundle.getString("sat"), bundle.getString("sun")};
        for (int i = 0; i < 7; i++) {
            Label dayLabel = new Label(days[i]);
            dayLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333; -fx-font-size: 18;");
            dayLabel.setMinWidth(70);
            dayLabel.setAlignment(Pos.CENTER);
            grid.add(dayLabel, i, 0);
        }

        int day = 1, daysInMonth = yearMonth.lengthOfMonth();
        for (int row = 1; row <= 6; row++) {
            for (int col = 0; col < 7; col++) {
                int cellIndex = (row - 1) * 7 + col + 1;
                VBox cellBox = new VBox(2);
                cellBox.setAlignment(Pos.TOP_CENTER);
                cellBox.setMinSize(70, 55);

                Label dayCell = new Label();
                dayCell.setMinSize(70, 30);
                dayCell.setAlignment(Pos.CENTER);

                boolean isWeekend = (col == 5 || col == 6);

                if (cellIndex >= firstDayOfWeek && day <= daysInMonth) {
                    dayCell.setText(String.valueOf(day));
                    cellBox.getStyleClass().add("calendar-cell");
                    if (yearMonth.getYear() == today.getYear() &&
                            yearMonth.getMonth() == today.getMonth() &&
                            day == today.getDayOfMonth()) {
                        cellBox.getStyleClass().add("calendar-cell-today");
                    } else if (isWeekend) {
                        cellBox.getStyleClass().add("calendar-cell-weekend");
                    }
                    List<Event> todaysEvents = getEvents(yearMonth.getYear(), yearMonth.getMonthValue(), day, searchBar.getText());
                    if (!todaysEvents.isEmpty()) {
                        HBox dots = new HBox(2);
                        dots.setAlignment(Pos.CENTER);
                        for (Event ev : todaysEvents) {
                            Label dot = new Label("●");
                            dot.setStyle("-fx-text-fill: " + ev.getColor() + "; -fx-font-size: 12;");
                            dots.getChildren().add(dot);
                        }
                        cellBox.getChildren().addAll(dayCell, dots);
                        cellBox.setStyle(cellBox.getStyle() + "-fx-border-color: " + todaysEvents.get(0).getColor() + "; -fx-border-width: 2;");
                    } else {
                        cellBox.getChildren().add(dayCell);
                    }
                    int finalDay = day;
                    cellBox.setOnMouseClicked(e -> {
                        if (e.getButton() == MouseButton.PRIMARY) {
                            showDayModal(yearMonth.getYear(), yearMonth.getMonthValue(), finalDay);
                        }
                    });
                    day++;
                } else {
                    cellBox.getChildren().add(dayCell);
                }
                grid.add(cellBox, col, row);
            }
        }
        return grid;
    }

    private void showDayModal(int year, int month, int day) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(bundle.getString("add_event"));

        VBox box = new VBox(15);
        box.setPadding(new Insets(20));
        box.setAlignment(Pos.TOP_CENTER);
        box.getStyleClass().add("glass-modal");

        List<Event> todaysEvents = getEvents(year, month, day, "");

        Label dateLabel = new Label(year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day));
        dateLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        ListView<Event> eventList = new ListView<>();
        eventList.getItems().addAll(todaysEvents);
        eventList.setCellFactory(list -> new ListCell<Event>() {
            @Override
            protected void updateItem(Event e, boolean empty) {
                super.updateItem(e, empty);
                if (empty || e == null) setText(null);
                else setText("[" + e.category + (e.recurring ? " ♻" : "") + "] " + e.title +
                        (e.note.isEmpty() ? "" : " - " + e.note) +
                        (e.reminderTime != null ? " ⏰" + e.reminderTime : ""));
            }
        });

        TextField titleField = new TextField();
        titleField.setPromptText(bundle.getString("event_title"));
        TextField noteField = new TextField();
        noteField.setPromptText(bundle.getString("event_note"));
        ComboBox<String> catCombo = new ComboBox<>();
        catCombo.getItems().addAll(bundle.getString("work"), bundle.getString("study"),
                bundle.getString("health"), bundle.getString("personal"), bundle.getString("other"));
        catCombo.setValue(bundle.getString("other"));

        CheckBox recurringBox = new CheckBox(bundle.getString("recurring"));
        TimePicker reminderPicker = new TimePicker();

        Button addBtn = new Button(bundle.getString("save"));
        addBtn.setOnAction(e -> {
            if (!titleField.getText().trim().isEmpty()) {
                Event ev = new Event(
                        titleField.getText(),
                        noteField.getText(),
                        catCombo.getValue(),
                        year, month, day,
                        recurringBox.isSelected(),
                        reminderPicker.getValue()
                );
                events.add(ev);
                eventList.getItems().add(ev);
                updateCalendar();
                titleField.clear(); noteField.clear(); catCombo.setValue(bundle.getString("other"));
                recurringBox.setSelected(false); reminderPicker.setValue(null);
            }
        });

        Button editBtn = new Button(bundle.getString("edit_event"));
        Button delBtn = new Button(bundle.getString("delete_event"));
        editBtn.setDisable(true); delBtn.setDisable(true);

        eventList.getSelectionModel().selectedItemProperty().addListener((obs, o, selected) -> {
            editBtn.setDisable(selected == null);
            delBtn.setDisable(selected == null);
            if (selected != null) {
                titleField.setText(selected.title);
                noteField.setText(selected.note);
                catCombo.setValue(selected.category);
                recurringBox.setSelected(selected.recurring);
                reminderPicker.setValue(selected.reminderTime);
            }
        });

        editBtn.setOnAction(e -> {
            Event selected = eventList.getSelectionModel().getSelectedItem();
            if (selected != null && !titleField.getText().trim().isEmpty()) {
                selected.title = titleField.getText();
                selected.note = noteField.getText();
                selected.category = catCombo.getValue();
                selected.recurring = recurringBox.isSelected();
                selected.reminderTime = reminderPicker.getValue();
                eventList.refresh();
                updateCalendar();
            }
        });

        delBtn.setOnAction(e -> {
            Event selected = eventList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                events.remove(selected);
                eventList.getItems().remove(selected);
                updateCalendar();
            }
        });

        Button closeBtn = new Button(bundle.getString("close"));
        closeBtn.setOnAction(e -> dialog.close());

        HBox btnBox = new HBox(10, addBtn, editBtn, delBtn, closeBtn);
        btnBox.setAlignment(Pos.CENTER);

        box.getChildren().addAll(dateLabel, eventList, titleField, noteField, catCombo, recurringBox, reminderPicker, btnBox);
        Scene scene = new Scene(box, 440, 480);
        scene.getStylesheets().add(getClass().getResource("calendar.css").toExternalForm());
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private VBox createSidebarMenu() {
        VBox sidebar = new VBox(18);
        sidebar.setPadding(new Insets(24));
        sidebar.getStyleClass().add("sidebar-glass");
        sidebar.setPrefWidth(220);
        sidebar.setVisible(false);

        Label settings = new Label(bundle.getString("settings"));
        settings.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
        settings.setOnMouseClicked(e -> openSettingsDialog());
        sidebar.getChildren().add(settings);

        return sidebar;
    }

    private void openSettingsDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Settings");

        VBox box = new VBox(18);
        box.setPadding(new Insets(20));
        box.setAlignment(Pos.TOP_CENTER);
        box.getStyleClass().add("glass-modal");

        CheckBox darkModeOption = new CheckBox("Enable Dark Mode");
        darkModeOption.setSelected(darkModeToggle.isSelected());
        darkModeOption.selectedProperty().addListener((obs, oldVal, newVal) -> {
            darkModeToggle.setSelected(newVal);
            applyTheme(newVal ? "Dark" : themePicker.getValue());
        });

        Label themeLabel = new Label("Select Theme:");
        ComboBox<String> themeSelect = new ComboBox<>();
        themeSelect.getItems().addAll("Light", "Dark", "Blue", "Green");
        themeSelect.setValue(themePicker.getValue());
        themeSelect.setOnAction(e -> {
            themePicker.setValue(themeSelect.getValue());
            applyTheme(themeSelect.getValue());
        });

        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e -> dialog.close());

        box.getChildren().addAll(darkModeOption, themeLabel, themeSelect, closeBtn);

        Scene scene = new Scene(box, 300, 200);
        scene.getStylesheets().add(getClass().getResource("calendar.css").toExternalForm());
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void toggleSidebar() {
        sidebarVisible = !sidebarVisible;
        sidebarMenu.setVisible(sidebarVisible);
    }

    private List<Event> getEvents(int year, int month, int day, String filter) {
        List<Event> result = new ArrayList<>();
        for (Event e : events) {
            boolean match = (e.year == year && e.month == month && e.day == day)
                    || (e.recurring && e.day == day && e.month == month);
            if (match && (filter == null || filter.isEmpty() || e.title.toLowerCase().contains(filter.toLowerCase())))
                result.add(e);
        }
        return result;
    }

    private void applyTheme(String theme) {
        mainScene.getRoot().setStyle("");
        mainScene.getStylesheets().removeIf(s -> s.contains("calendar.css"));
        mainScene.getStylesheets().add(getClass().getResource("calendar.css").toExternalForm());
        switch (theme) {
            case "Dark":
                mainScene.getRoot().setStyle("-fx-base: #23272f; -fx-background: #23272f; -fx-text-fill: #eee;");
                break;
            case "Blue":
                mainScene.getRoot().setStyle("-fx-base: #e3f2fd; -fx-background: #90caf9;");
                break;
            case "Green":
                mainScene.getRoot().setStyle("-fx-base: #e8f5e9; -fx-background: #a5d6a7;");
                break;
            default:
                mainScene.getRoot().setStyle("-fx-base: #fff; -fx-background: #fff;");
        }
    }

    private void checkReminders() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);
        for (Event e : events) {
            if (e.reminderTime != null &&
                    e.year == today.getYear() && e.month == today.getMonthValue() && e.day == today.getDayOfMonth() &&
                    e.reminderTime.equals(now)) {
                showReminder(e);
            }
            if (e.recurring && e.reminderTime != null &&
                    e.day == today.getDayOfMonth() && e.month == today.getMonthValue() &&
                    e.reminderTime.equals(now)) {
                showReminder(e);
            }
        }
    }

    private void showReminder(Event e) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Event Reminder");
        alert.setHeaderText(e.title + " (" + e.category + ")");
        alert.setContentText(e.note + "\n" + e.year + "-" + e.month + "-" + e.day + " " + e.reminderTime);
        alert.show();
        PauseTransition delay = new PauseTransition(Duration.seconds(10));
        delay.setOnFinished(ev -> alert.close());
        delay.play();
    }

    static class TimePicker extends ComboBox<LocalTime> {
        TimePicker() {
            setPromptText("HH:mm");
            for (int h = 0; h < 24; h++)
                for (int m = 0; m < 60; m += 15)
                    getItems().add(LocalTime.of(h, m));
            setEditable(false);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
