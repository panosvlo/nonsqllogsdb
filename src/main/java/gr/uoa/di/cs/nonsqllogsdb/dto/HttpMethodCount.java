package gr.uoa.di.cs.nonsqllogsdb.dto;

public class HttpMethodCount {
    private String method;
    private long count;

    // Constructor, getters, and setters
    public HttpMethodCount(String method, long count) {
        this.method = method;
        this.count = count;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
