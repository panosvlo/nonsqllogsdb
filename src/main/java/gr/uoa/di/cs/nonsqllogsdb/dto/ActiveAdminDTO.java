package gr.uoa.di.cs.nonsqllogsdb.dto;

public class ActiveAdminDTO {
    private String username;
    private int totalUpvotes;

    public ActiveAdminDTO(String username, int totalUpvotes) {
        this.username = username;
        this.totalUpvotes = totalUpvotes;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTotalUpvotes() {
        return totalUpvotes;
    }

    public void setTotalUpvotes(int totalUpvotes) {
        this.totalUpvotes = totalUpvotes;
    }
}
