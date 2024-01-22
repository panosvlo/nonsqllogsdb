package gr.uoa.di.cs.nonsqllogsdb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Date;
import java.util.List;

@Document(collection = "logs")
public class Log {

    @Id
    private String id;

    @DBRef
    private LogType logType;

    private Date timestamp;
    private String sourceIp;
    private String destinationIp;
    private List<LogDetail> details;

    // Constructors
    public Log() {
    }

    public Log(LogType logType, Date timestamp, String sourceIp, String destinationIp, List<LogDetail> details) {
        this.logType = logType;
        this.timestamp = timestamp;
        this.sourceIp = sourceIp;
        this.destinationIp = destinationIp;
        this.details = details;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LogType getLogType() {
        return logType;
    }

    public void setLogType(LogType logType) {
        this.logType = logType;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public String getDestinationIp() {
        return destinationIp;
    }

    public void setDestinationIp(String destinationIp) {
        this.destinationIp = destinationIp;
    }

    public List<LogDetail> getDetails() {
        return details;
    }

    public void setDetails(List<LogDetail> details) {
        this.details = details;
    }
}
