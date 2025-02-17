package se.lu.ics.models;

import java.time.LocalDate;

public class Project {
    private String projectNo;
    private String projectName;
    private LocalDate projectStartDate;
    private LocalDate projectEndDate;
    private String projectStatus; // ProjectStatus is a computed column in SQL Server

    /**
     * Main constructor for initializing Project objects.
     * Since ProjectStatus is a computed column, it is set through setProjectStatus
     * in ProjectDao(), and not in constructor.
     * 
     * @param projectNo        The project number.
     * @param projectName      The project name.
     * @param projectStartDate The start date of the project.
     * @param projectEndDate   The end date of the project.
     */
    public Project(String projectNo, String projectName, LocalDate projectStartDate, LocalDate projectEndDate) {
        this.projectNo = projectNo;
        this.projectName = projectName;
        this.projectStartDate = projectStartDate;
        this.projectEndDate = projectEndDate;
    }

    /**
     * Secondary constructor used in mapToProject() in ProjectDao to retrieve all values for Project
     * instance.
     *
     * @param projectNo        the project number
     * @param projectName      the name of the project
     * @param projectStartDate the start date of the project
     * @param projectEndDate   the end date of the project
     * @param projectStatus    the status of the project
     * 
     */
    public Project(String projectNo, String projectName, LocalDate projectStartDate, LocalDate projectEndDate,
            String projectStatus) {
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

    public LocalDate getProjectEndDate() {
        return projectEndDate;
    }

    public void setProjectEndDate(LocalDate projectEndDate) {
        this.projectEndDate = projectEndDate;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    // For in terminal testing purposes
    @Override
    public String toString() {
        return "Project{" +
                "projectNo='" + projectNo + '\'' +
                ", projectName='" + projectName + '\'' +
                ", projectStartDate=" + projectStartDate +
                ", projectEndDate=" + projectEndDate +
                ", projectStatus='" + projectStatus + '\'' +
                '}';
    }
}
