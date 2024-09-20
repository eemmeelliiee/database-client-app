package se.lu.ics.models;

import java.time.LocalDate;

public class Consultant {
    private String EmpNo;
    private String EmpFirstName;
    private String EmpLastName;
    private String EmpTitle;
    private LocalDate EmpStartDate;

    public Consultant(String EmpNo, String EmpFirstName, String EmpLastName, String EmpTitle, LocalDate StartDate) {
        this.EmpNo = EmpNo;
        this.EmpFirstName = EmpFirstName;
        this.EmpLastName = EmpLastName;
        this.EmpTitle = EmpTitle;
        this.EmpStartDate = StartDate;
    }

    public String getEmpNo() {
        return EmpNo;
    }

    public void setEmpNo(String EmpNo) {
        this.EmpNo = EmpNo;
    }

    public String getEmpFirstName() {
        return EmpFirstName;
    }

    public void setEmpFirstName(String EmpFirstName) {
        this.EmpFirstName = EmpFirstName;
    }

    public String getEmpLastName() {
        return EmpLastName;
    }

    public void setEmpLastName(String EmpLastName) {
        this.EmpLastName = EmpLastName;
    }

    public String getEmpTitle() {
        return EmpTitle;
    }

    public void setEmpTitle(String EmpTitle) {
        this.EmpTitle = EmpTitle;
    }

    public LocalDate getEmpStartDate() {
        return EmpStartDate;
    }

    public void setEmpStartDate(LocalDate StartDate) {
        this.EmpStartDate = StartDate;
    }
}
