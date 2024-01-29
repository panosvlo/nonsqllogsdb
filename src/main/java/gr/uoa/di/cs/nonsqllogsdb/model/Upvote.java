package gr.uoa.di.cs.nonsqllogsdb.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "upvotes")
public class Upvote {
    @Id
    private String id;
    private ObjectId userId;
    private ObjectId logId;
    private Date upvotedAt = new Date();

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public ObjectId getLogId() {
        return logId;
    }

    public void setLogId(ObjectId logId) {
        this.logId = logId;
    }

    public Date getUpvotedAt() {
        return upvotedAt;
    }

    public void setUpvotedAt(Date upvotedAt) {
        this.upvotedAt = upvotedAt;
    }
}
