package se.lu.ics.controllers;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import java.time.LocalDate;

public class DatePickerTableCell<S> extends TableCell<S, LocalDate> {
    private final DatePicker datePicker;

    public DatePickerTableCell() {
        this.datePicker = new DatePicker();
        this.datePicker.setOnAction(event -> {
            if (isEditing()) {
                commitEdit(datePicker.getValue());
            }
        });
        setGraphic(datePicker);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    @Override
    public void startEdit() {
        super.startEdit();
        datePicker.setValue(getItem());
        setGraphic(datePicker);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    public void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                datePicker.setValue(item);
                setGraphic(datePicker);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            } else {
                setText(item != null ? item.toString() : null);
                setContentDisplay(ContentDisplay.TEXT_ONLY);
            }
        }
    }

    public static <S> Callback<TableColumn<S, LocalDate>, TableCell<S, LocalDate>> forTableColumn() {
        return param -> new DatePickerTableCell<>();
    }
}