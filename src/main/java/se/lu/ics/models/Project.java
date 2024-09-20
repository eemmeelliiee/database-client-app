package se.lu.ics.models;

import java.time.LocalDate;

public class Project {
    private String ProjectNo;
    private String ProjectName;
    private LocalDate ProjectStartDate;
    
    public Project(String ProjectNo, String ProjectName) {
        this.ProjectNo = ProjectNo;
        this.ProjectName = ProjectName;
        this.ProjectStartDate = LocalDate.now();
    }

    public String getProjectNo() {
        return ProjectNo;
    }

    public void setProjectNo(String ProjectNo) {
        this.ProjectNo = ProjectNo;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String ProjectName) {
        this.ProjectName = ProjectName;
    }

    public LocalDate getProjectStartDate() {
        return ProjectStartDate;
    }

    public void setProjectStartDate(LocalDate ProjectStartDate) {
        this.ProjectStartDate = ProjectStartDate;
    }
}

