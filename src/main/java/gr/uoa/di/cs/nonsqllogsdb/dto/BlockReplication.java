package gr.uoa.di.cs.nonsqllogsdb.dto;

import java.util.Date;

public class BlockReplication {
    private String blockId;
    private Date timestamp;

    // Constructor
    public BlockReplication(String blockId, Date timestamp) {
        this.blockId = blockId;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
