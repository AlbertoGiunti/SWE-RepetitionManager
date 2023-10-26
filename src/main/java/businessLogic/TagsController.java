package businessLogic;

import dao.TagDAO;
import domainModel.Tags.*;

public class TagsController {
    public final TagDAO tagDAO;

    public TagsController(TagDAO tagDAO){
        this.tagDAO = tagDAO;
    }

    public int createTag(String tag, String tagType) throws Exception {
        Tag t = this.tagDAO.getTagByInfo(tag, tagType);
        if ( t == null ){
            switch (tagType){
                case "IsOnline":
                    TagIsOnline tagIsOnline = new TagIsOnline(this.tagDAO.getNextTagID(), tag);
                    return tagIsOnline.getIdTag();
                case "Level":
                    TagLevel tagLevel = new TagLevel(this.tagDAO.getNextTagID(), tag);
                case "Subject":
                    t = new TagSubject(this.tagDAO.getNextTagID(), tag);
                case "Zone":
                    t = new TagZone(this.tagDAO.getNextTagID(), tag);
                default: throw new IllegalArgumentException("Invalid tag type");
            }
        }

        return t.getIdTag();
    }

    public boolean deleteTag(int idTag) throws Exception{
        //TODO
        return this.tagDAO.removeTag(idTag);
    }
}
