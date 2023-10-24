package dao;

import domainModel.Lesson;
import domainModel.Tags.*;

import java.sql.SQLException;
import java.util.List;

public interface TagDAO {

    public Tag getTag(Integer idTag) throws Exception;

    public List<Tag> getAllTags() throws Exception;

    /**
     * Add a tag to a lesson, also match the tag to the lesson
     *
     * @param lesson
     * @param tag
     * @throws Exception
     */
    public void addTag(Lesson lesson, Tag tag) throws Exception;

    /**
     * Remove a tag
     *
     * @param idTag
     * @return
     * @throws Exception
     */

    public boolean removeTag(Integer idTag) throws Exception;

    public int getLastTagID() throws Exception;

    public List<Tag> getTagsByLesson(Integer idLesson) throws Exception;
}
