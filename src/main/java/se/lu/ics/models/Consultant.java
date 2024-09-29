package se.lu.ics.models;

import java.time.LocalDate;

public class Consultant {
    private String empNo;
    private String empFirstName;
    private String empLastName;
    private String empTitle;
    private LocalDate empStartDate;
    private double totalWorkHours; // Temporary attribute for specific use case getTotal

    /**
     * Constructor
     *
     * @param empNo        the employee number of the consultant
     * @param empFirstName the first name of the consultant
     * @param empLastName  the last name of the consultant
     * @param empTitle     the title of the consultant
     * @param empStartDate the start date of the consultant
     */
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

    // Getters and setters for temporary attribute
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
