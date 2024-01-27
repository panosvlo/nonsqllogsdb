package gr.uoa.di.cs.nonsqllogsdb.dto;

import java.util.Date;
import java.util.Set;

public class BlockOperation {
    private String blockId;
    private Set<Date> operationDates;

    public BlockOperation(String blockId, Set<Date> operationDates) {
        this.blockId = blockId;
        this.operationDates = operationDates;
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public Set<Date> getOperationDates() {
        return operationDates;
    }

    public void setOperationDates(Set<Date> operationDates) {
        this.operationDates = operationDates;
    }
}
