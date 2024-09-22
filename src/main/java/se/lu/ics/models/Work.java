package se.lu.ics.models;

public class Work {

    private String projectNo;
    private String consultantNo;
    private double workHours;

    public Work(String projectNo, String consultantNo, double workHours) {
        this.projectNo = projectNo;
        this.consultantNo = consultantNo;
        this.workHours = workHours;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getConsultantNo() {
        return consultantNo;
    }

    public void setConsultantNo(String consultantNo) {
        this.consultantNo = consultantNo;
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
                ", consultantNo='" + consultantNo + '\'' +
                ", workHours=" + workHours +
                '}';
    }
}