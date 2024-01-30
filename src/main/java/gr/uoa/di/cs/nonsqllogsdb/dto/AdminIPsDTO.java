package gr.uoa.di.cs.nonsqllogsdb.dto;

import java.util.List;

public class AdminIPsDTO {
    private String username;
    private int distinctIPCount;
    private List<String> sourceIPs;

    // Constructors, Getters and Setters

    public AdminIPsDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getDistinctIPCount() {
        return distinctIPCount;
    }

    public void setDistinctIPCount(int distinctIPCount) {
        this.distinctIPCount = distinctIPCount;
    }

    public List<String> getSourceIPs() {
        return sourceIPs;
    }

    public void setSourceIPs(List<String> sourceIPs) {
        this.sourceIPs = sourceIPs;
    }
}
