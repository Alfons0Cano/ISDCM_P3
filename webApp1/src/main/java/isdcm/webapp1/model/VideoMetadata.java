package isdcm.webapp1.model;

import java.sql.Date;
import java.sql.Time;
import java.io.Serializable;

/**
 * Model class representing video metadata.
 * Used for extracting and storing information about videos.
 */
public class VideoMetadata implements Serializable {
    private final Date creationDate;
    private final Time duration;
    private final String format;
    
    public VideoMetadata(Date creationDate, Time duration, String format) {
        this.creationDate = creationDate;
        this.duration = duration;
        this.format = format;
    }
    
    public Date getCreationDate() {
        return creationDate;
    }
    
    public Time getDuration() {
        return duration;
    }
    
    public String getFormat() {
        return format;
    }
    
    @Override
    public String toString() {
        return "VideoMetadata{" +
                "creationDate=" + creationDate +
                ", duration=" + duration +
                ", format='" + format + '\'' +
                '}';
    }
}