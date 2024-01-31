package gr.uoa.di.cs.nonsqllogsdb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "log_types")
public class LogType {

    @Id
    private String id;
    @Field("type_name")
    private String typeName;

    // Constructors
    public LogType() {
    }

    public LogType(String typeName) {
        this.typeName = typeName;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
