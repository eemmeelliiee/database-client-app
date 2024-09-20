package se.lu.ics.models;

import java.time.LocalDate;

public class Milestone {
    private String MilestoneNo;
    private String MilestoneName;
    private LocalDate MilestoneDate;

    public Milestone(String MilestoneNo, String MilestoneName) {
        this.MilestoneNo = MilestoneNo;
        this.MilestoneName = MilestoneName;
        this.MilestoneDate = LocalDate.now();
    }

    public String getMilestoneNo() {
        return MilestoneNo;
    }

    public void setMilestoneNo(String MilestoneNo) {
        this.MilestoneNo = MilestoneNo;
    }

    public String getMilestoneName() {
        return MilestoneName;
    }

    public void setMilestoneName(String MilestoneName) {
        this.MilestoneName = MilestoneName;
    }
    
    public LocalDate getMilestoneDate() {
        return MilestoneDate;
    }

    public void setMilestoneDate(LocalDate MilestoneDate) {
        this.MilestoneDate = MilestoneDate;
    }
}
