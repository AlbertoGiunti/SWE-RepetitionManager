package domainModel;

import java.util.Objects;
import java.time.LocalDateTime;

public class Lesson {
    // This is the ID of the advertisement
    private final int id;
    // This is the title of the advertisement
    private String title;
    // This is the description of the advertisement
    private String description;
    // This is the start date and time of the lesson and the end date and time of the lesson
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    // This is the price of the lesson
    private double price;
    // This is the CF of the tutor
    private final String tutorCF;

    public Lesson(int id, String title, String description, LocalDateTime startTime, LocalDateTime endTime, double price, String tutorCF) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.tutorCF = tutorCF;
    }

    // GETTERS

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public double getPrice() {
        return price;
    }

    public String getTutorCF() {
        return tutorCF;
    }
}
