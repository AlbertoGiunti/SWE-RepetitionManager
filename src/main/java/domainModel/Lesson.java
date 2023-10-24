package domainModel;

import domainModel.State.*;
import domainModel.Tags.Tag;

import java.util.ArrayList;
import java.util.List;
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
    // This is the state of the lesson
    private State state;
    // This is the list of tags
    List<Tag> tags = new ArrayList<>();


    public Lesson(int id, String title, String description, LocalDateTime startTime, LocalDateTime endTime, double price, String tutorCF) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.tutorCF = tutorCF;

        this.state = new Available();
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

    public String getGetState() {
        return state.getState();
    }

    public List<Tag> getTags(){
        return tags;
    }

    // SETTERS
    public void setState(State state) {
        this.state = state;
    }

    // TAGS METHODS
    public void addTag(Tag newTag){
        this.tags.add(newTag);
    }

    public boolean removeTag(String tagType, String tag){
        boolean removed = false;
        for (Tag t: tags){
            if(t.getTypeOfTag() == tagType && t.getTag() == tag){
                this.tags.remove(t);
                removed = true;
            }
        }

        return removed;
    }

}
