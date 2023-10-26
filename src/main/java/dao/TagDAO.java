package dao;

import domainModel.Tags.*;

import java.util.List;

public interface TagDAO {

    /**
     * Get a tag by his id
     *
     * @param idTag the id of the tag
     * @throws Exception
     */
    public Tag getTagByID(Integer idTag) throws Exception;

    /**
     * Get tag by his info
     *
     * @param tag
     * @param tagType
     *
     * @return A single tag
     * @throws Exception
     */
    public Tag getTagByInfo(String tag, String tagType) throws Exception;

    /**
     * Get all existing tags
     *
     * @return List of tags
     * @throws Exception
     */
    public List<Tag> getAllTags() throws Exception;

    /**
     * Add a tag to the DataBase
     *
     * @param tag
     * @throws Exception
     */
    public void addTag(Tag tag) throws Exception;

    /**
     * Attach a tag to a lesson
     *
     * @param idTag
     * @param idLesson
     * @throws Exception
     */
    public void attachTag(Integer idTag, Integer idLesson) throws Exception;

    /**
     * Remove a tag from the db
     *
     * @param idTag
     * @return
     * @throws Exception
     */
    public boolean removeTag(Integer idTag) throws Exception;

    /**
     * Detach a tag from a lesson
     *
     * @param idTag
     * @param idLesson
     * @throws Exception
     */
    public void detachTag(Integer idTag, Integer idLesson) throws Exception;

    /**
     * Get the id of the last tag added
     *
     * @return
     * @throws Exception
     */
    public int getNextTagID() throws Exception;

    /**
     * Get all the tags of a specific lesson
     *
     * @param idLesson
     * @return
     * @throws Exception
     */
    public List<Tag> getTagsByLesson(Integer idLesson) throws Exception;
}
