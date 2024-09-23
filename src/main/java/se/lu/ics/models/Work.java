package se.lu.ics.models;

public class Work {

    private String empNo;
    private String projectNo;
    private double workHours;

    public Work(String empNo, String projectNo, double workHours) {
        this.empNo = empNo;
        this.projectNo = projectNo;
        this.workHours = workHours;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getEmpNo() {
        return empNo;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }

    public double getWorkHours() {
        return workHours;
    }

    public void setWorkHours(double workHours) {
        this.workHours = workHours;
    }

    // For in terminal testing purposes
    @Override
    public String toString() {
        return "Work{" +
                "projectNo='" + projectNo + '\'' +
                ", empNo='" + empNo + '\'' +
                ", workHours=" + workHours +
                '}';
    }
}