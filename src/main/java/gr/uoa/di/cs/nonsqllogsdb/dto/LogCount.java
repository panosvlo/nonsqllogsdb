package gr.uoa.di.cs.nonsqllogsdb.dto;

public class LogCount {
    private String typeName;
    private long count;

    // Constructor, getters, and setters
    public LogCount(String typeName, long count) {
        this.typeName = typeName;
        this.count = count;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
