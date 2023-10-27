package dao;

import domainModel.Tags.*;

import java.util.List;

public interface TagDAO {
    /**
     * Get tag by his info
     *
     * @param tag
     * @param tagType
     *
     * @return A single tag
     * @throws Exception
     */
    public Tag getTag(String tag, String tagType) throws Exception;

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
     * @param tagToAttach
     * @param idLesson
     * @throws Exception
     */
    public void attachTag(Integer idLesson, Tag tagToAttach) throws Exception;

    /**
     * Remove a tag from the db
     *
     * @param tag
     * @param tagType
     * @return
     * @throws Exception
     */
    public boolean removeTag(String tag, String tagType) throws Exception;

    /**
     * Detach a tag from a lesson
     *
     * @param tagToDetach
     * @param idLesson
     * @throws Exception
     */
    public boolean detachTag(Integer idLesson, Tag tagToDetach) throws Exception;


    /**
     * Get all the tags of a specific lesson
     *
     * @param idLesson
     * @return
     * @throws Exception
     */
    public List<Tag> getTagsByLesson(Integer idLesson) throws Exception;
}
