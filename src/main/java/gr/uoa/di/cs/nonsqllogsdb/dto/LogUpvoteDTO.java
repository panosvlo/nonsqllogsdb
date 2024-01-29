package gr.uoa.di.cs.nonsqllogsdb.dto;

public class LogUpvoteDTO {
    private String id;
    private int upvoteCount;

    public LogUpvoteDTO(String id, int upvoteCount) {
        this.id = id;
        this.upvoteCount = upvoteCount;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUpvoteCount() {
        return upvoteCount;
    }

    public void setUpvoteCount(int upvoteCount) {
        this.upvoteCount = upvoteCount;
    }
}
