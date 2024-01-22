package gr.uoa.di.cs.nonsqllogsdb.model;

public class LogDetail {

    private String key;
    private String value;

    // Constructors
    public LogDetail() {
    }

    public LogDetail(String key, String value) {
        this.key = key;
        this.value = value;
    }

    // Getters and Setters
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
