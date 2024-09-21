package se.lu.ics.models;

import java.time.LocalDate;

public class Project {
    private String projectNo;
    private String projectName;
    private LocalDate projectStartDate;
    private LocalDate projectEndDate;
    private String projectStatus; // ProjectStatus is a computed column in SQL Server
    
    // projectStatus is thus set through setProjectStatus() in ProjectDao 
    public Project(String projectNo, String projectName, LocalDate projectStartDate, LocalDate projectEndDate) {
        this.projectNo = projectNo;
        this.projectName = projectName;
        this.projectStartDate = projectStartDate;
        this.projectEndDate = projectEndDate;
    }

    // Used in mapToProject() in ProjectDao to retrieve all values for project instance
    public Project(String projectNo, String projectName, LocalDate projectStartDate, LocalDate projectEndDate, String projectStatus) {
        this.projectNo = projectNo;
        this.projectName = projectName;
        this.projectStartDate = projectStartDate;
        this.projectEndDate = projectEndDate;
        this.projectStatus = projectStatus;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public LocalDate getProjectStartDate() {
        return projectStartDate;
    }

    public void setProjectStartDate(LocalDate projectStartDate) {
        this.projectStartDate = projectStartDate;
    }

    public LocalDate getProjectEndDate(){
        return projectEndDate;
    }

    public void setProjectEndDate(LocalDate projectEndDate){
        this.projectEndDate = projectEndDate;
    }
   
    public String getProjectStatus(){
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus){
        this.projectStatus = projectStatus;
    }
}

