package gr.uoa.di.cs.nonsqllogsdb.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Document(collection = "logs")
public class Log {

    @Id
    private String id;

    @DBRef
    private LogType logType;

    private Date timestamp;
    @Field("source_ip")
    private String source_ip;
    private String destinationIp;
    private List<LogDetail> details;

    private int upvoteCount = 0;
    @Field("log_type_id")
    private ObjectId log_type_id;

    // Constructors
    public Log() {
    }

    public Log(ObjectId log_type_id, Date timestamp, String sourceIp, String destinationIp, List<LogDetail> details) {
        this.log_type_id = log_type_id;
        this.logType = logType;
        this.timestamp = timestamp;
        this.source_ip = source_ip;
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
        return source_ip;
    }

    public void setSourceIp(String sourceIp) {
        this.source_ip = sourceIp;
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

    public int getUpvoteCount() {
        return upvoteCount;
    }

    public void setUpvoteCount(int upvoteCount) {
        this.upvoteCount = upvoteCount;
    }
    public ObjectId  getLogTypeId() {
        return log_type_id;
    }

    public void setLogTypeId(ObjectId  logTypeId) {
        this.log_type_id = logTypeId;
    }

}
