package gr.uoa.di.cs.nonsqllogsdb.dto;

public class UserBlockDTO {
    private String blockId;

    // Constructor
    public UserBlockDTO(String blockId) {
        this.blockId = blockId;
    }

    // Getters and Setters
    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }
}
