// LogFileUploadDTO.java
package gr.uoa.di.cs.nonsqllogsdb.dto;

import org.springframework.web.multipart.MultipartFile;

public class LogFileUploadDTO {
    private MultipartFile file;
    private String logTypeName; // changed from logTypeId to logTypeName

    // Getters and Setters
    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getLogTypeName() {
        return logTypeName;
    }

    public void setLogTypeName(String logTypeName) {
        this.logTypeName = logTypeName;
    }
}
