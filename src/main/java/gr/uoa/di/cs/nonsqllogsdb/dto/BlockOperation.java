package gr.uoa.di.cs.nonsqllogsdb.dto;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class BlockOperation {
    private String blockId;
    private List<Date> operationDates;

    // Constructor
    public BlockOperation(String blockId, List<Date> operationDates) {
        this.blockId = blockId;
        this.operationDates = operationDates;
    }

    // Getters and setters
    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public List<Date> getOperationDates() {
        return operationDates;
    }

    public void setOperationDates(List<Date> operationDates) {
        this.operationDates = operationDates;
    }
}
