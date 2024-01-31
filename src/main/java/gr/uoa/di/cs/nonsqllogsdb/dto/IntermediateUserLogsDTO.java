package gr.uoa.di.cs.nonsqllogsdb.dto;

import gr.uoa.di.cs.nonsqllogsdb.model.Log;
import java.util.List;

public class IntermediateUserLogsDTO {
    private String email;
    private List<String> usernames;
    private List<Log> logs;

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

    public List<Log> getLogs() {
        return logs;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }
}
