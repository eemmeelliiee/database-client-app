package se.lu.ics.models;

import java.time.LocalDate;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class Consultant {
    private String empNo;
    private String empFirstName;
    private String empLastName;
    private StringProperty empTitle;
    private ObjectProperty<LocalDate> empStartDate;

    public Consultant(String empNo, String empFirstName, String empLastName, String empTitle, LocalDate empStartDate) {
        this.empNo = empNo;
        this.empFirstName = empFirstName;
        this.empLastName = empLastName;
        this.empTitle = new SimpleStringProperty(empTitle);
        this.empStartDate = new SimpleObjectProperty<>(empStartDate);
    }

    public String getEmpNo() {
        return empNo;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }

    public String getEmpFirstName() {
        return empFirstName;
    }

    public void setEmpFirstName(String empFirstName) {
        this.empFirstName = empFirstName;
    }

    public String getEmpLastName() {
        return empLastName;
    }

    public void setEmpLastName(String empLastName) {
        this.empLastName = empLastName;
    }

    public String getEmpTitle() {
        return empTitle.get();
    }

    public void setEmpTitle(String empTitle) {
        this.empTitle.set(empTitle);
    }

    public StringProperty empTitleProperty() {
        return empTitle;
    }

    public void setEmpTitle(StringProperty empTitle) {
        this.empTitle = empTitle;
    }

    public LocalDate getEmpStartDate() {
        return empStartDate.get();
    }

    public void setEmpStartDate(LocalDate empStartDate) {
        this.empStartDate.set(empStartDate);
    }

    public ObjectProperty<LocalDate> empStartDateProperty() {
        return empStartDate;
    }

    public void setEmpStartDate(ObjectProperty<LocalDate> empStartDate) {
        this.empStartDate = empStartDate;
    }

    // For in terminal testing purposes
    @Override
    public String toString() {
        return "Consultant{" +
                "empNo='" + empNo + '\'' +
                ", empFirstName='" + empFirstName + '\'' +
                ", empLastName='" + empLastName + '\'' +
                ", empTitle='" + empTitle + '\'' +
                ", empStartDate=" + empStartDate +
                '}';
    }
}
