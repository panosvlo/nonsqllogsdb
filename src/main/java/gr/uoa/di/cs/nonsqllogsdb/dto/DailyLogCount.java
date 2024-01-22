package gr.uoa.di.cs.nonsqllogsdb.dto;

public class DailyLogCount {
    private String day;  // Use String for day
    private long count;

    public DailyLogCount(String day, long count) {
        this.day = day;
        this.count = count;
    }

    // Getters and Setters
    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
