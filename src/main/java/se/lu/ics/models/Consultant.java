package se.lu.ics.models;

import java.time.LocalDate;

public class Consultant {
    private String empNo;
    private String empFirstName;
    private String empLastName;
    private String empTitle;
    private LocalDate empStartDate;
    // Temporary field for specific use case
    private double totalWorkHours;


    public Consultant(String empNo, String empFirstName, String empLastName, String empTitle, LocalDate empStartDate) {
        this.empNo = empNo;
        this.empFirstName = empFirstName;
        this.empLastName = empLastName;
        this.empTitle = empTitle;
        this.empStartDate = empStartDate;
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
        return empTitle;
    }

    public void setEmpTitle(String empTitle) {
        this.empTitle = empTitle;
    }

    public LocalDate getEmpStartDate() {
        return empStartDate;
    }

    public void setEmpStartDate(LocalDate empStartDate) {
        this.empStartDate = empStartDate;
    }

    // Getters and setters for temporary field
    public double getTotalWorkHours() {
        return totalWorkHours;
    }

    public void setTotalWorkHours(double totalWorkHours) {
        this.totalWorkHours = totalWorkHours;
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
