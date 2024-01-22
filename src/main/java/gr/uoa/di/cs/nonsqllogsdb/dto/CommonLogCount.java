package gr.uoa.di.cs.nonsqllogsdb.dto;

public class CommonLogCount {
    private String sourceIp;
    private long count;

    // Constructor, getters, and setters
    public CommonLogCount(String sourceIp, long count) {
        this.sourceIp = sourceIp;
        this.count = count;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
