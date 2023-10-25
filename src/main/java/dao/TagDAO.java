package dao;

import domainModel.Lesson;
import domainModel.Tags.*;

import java.util.List;

public interface TagDAO {

    /**
     * Get a tag by his id
     *
     * @param idTag the id of the tag
     * @throws Exception
     */
    public Tag getTag(Integer idTag) throws Exception;

    /**
     * Get all existing tags
     *
     * @return List of tags
     * @throws Exception
     */
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

    /**
     * Get the id of the last tag added
     *
     * @return
     * @throws Exception
     */
    public int getLastTagID() throws Exception;

    /**
     * Get all the tags of a specific lesson
     *
     * @param idLesson
     * @return
     * @throws Exception
     */
    public List<Tag> getTagsByLesson(Integer idLesson) throws Exception;
}
