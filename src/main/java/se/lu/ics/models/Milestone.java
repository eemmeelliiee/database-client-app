package se.lu.ics.models;

import java.time.LocalDate;

public class Milestone {
    private String milestoneNo;
    private String projectNo;
    private String milestoneName;
    private LocalDate milestoneDate;

    /**
     * Main constructor for initializing Milestone objects. 
     *
     * @param milestoneNo The milestone number.
     * @param projectNo The project number.
     * @param milestoneName The milestone name.
     * @param milestoneDate The milestone date.
     */
    public Milestone(String milestoneNo, String projectNo, String milestoneName, LocalDate milestoneDate) {
        this.milestoneNo = milestoneNo;
        this.projectNo = projectNo;
        this.milestoneName = milestoneName;
        this.milestoneDate = milestoneDate;
    }

    /**
     * Secondary constructor used in mapToMilestone() in MilestoneDao to retrieve all Milestones for Project.
     *
     * @param milestoneNo   The milestone number.
     * @param milestoneName The milestone name.
     * @param milestoneDate The milestone date.
     */
    public Milestone(String milestoneNo, String milestoneName, LocalDate milestoneDate) {
        this.milestoneNo = milestoneNo;
        this.milestoneName = milestoneName;
        this.milestoneDate = milestoneDate;
    }

    public String getMilestoneNo() {
        return milestoneNo;
    }

    public void setMilestoneNo(String milestoneNo) {
        this.milestoneNo = milestoneNo;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public void setMilestoneName(String milestoneName) {
        this.milestoneName = milestoneName;
    }

    public LocalDate getMilestoneDate() {
        return milestoneDate;
    }

    public void setMilestoneDate(LocalDate milestoneDate) {
        this.milestoneDate = milestoneDate;
    }

    // For in terminal testing purposes
    @Override
    public String toString() {
        return "Milestone{" +
                "milestoneNo='" + milestoneNo + '\'' +
                ", projectNo='" + projectNo + '\'' +
                ", milestoneName='" + milestoneName + '\'' +
                ", milestoneDate=" + milestoneDate +
                '}';
    }
}
