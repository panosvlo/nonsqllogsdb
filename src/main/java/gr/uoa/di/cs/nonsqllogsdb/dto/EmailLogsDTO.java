package gr.uoa.di.cs.nonsqllogsdb.dto;

import gr.uoa.di.cs.nonsqllogsdb.model.Log;
import java.util.List;
import java.util.Set;

public class EmailLogsDTO {
    private String email;
    private Set<String> usernames;
    private List<Log> logs;

    // Getters
    public String getEmail() {
        return email;
    }

    public Set<String> getUsernames() {
        return usernames;
    }

    public List<Log> getLogs() {
        return logs;
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsernames(Set<String> usernames) {
        this.usernames = usernames;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }
}
