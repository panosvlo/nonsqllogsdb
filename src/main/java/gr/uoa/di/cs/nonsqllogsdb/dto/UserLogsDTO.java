package gr.uoa.di.cs.nonsqllogsdb.dto;

import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class UserLogsDTO {
    private String email;
    private List<String> usernames;
    private List<LogInfo> logs;

    // Constructor
    public UserLogsDTO() {}

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
    }

    public List<LogInfo> getLogs() {
        return logs;
    }

    public void setLogs(List<LogInfo> logs) {
        this.logs = logs;
    }

    public static class LogInfo {
        private String id;
        private Date timestamp;
        private int upvoteCount;

        // Constructor
        public LogInfo() {}

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Date getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Date timestamp) {
            this.timestamp = timestamp;
        }

        public int getUpvoteCount() {
            return upvoteCount;
        }

        public void setUpvoteCount(int upvoteCount) {
            this.upvoteCount = upvoteCount;
        }
    }
}
