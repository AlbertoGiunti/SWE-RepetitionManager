package dao;

import domainModel.Lesson;
import domainModel.Tags.*;

import java.util.List;

public interface LessonDAO extends DAO <Lesson, Integer> {
    /**
     * Get all tutor's advertisements
     *
     * @param tutorCF the fiscal code of the tutor who created the advertisement
     */
    public List<Lesson> getTutorAdvertisements(String tutorCF) throws Exception;

    /**
     * Get last inserted advertisement
     */
    public int getLastLessonID() throws Exception;

    /**
     * Add a tag to the lesson
     *
     * @param idLesson The id of the lesson
     * @param newTag The tag to add
     */
    public void addTag(int idLesson, Tag newTag);

    /**
     * Change lesson state
     */
}
