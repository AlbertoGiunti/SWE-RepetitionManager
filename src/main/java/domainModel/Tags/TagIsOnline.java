package domainModel.Tags;

public class TagIsOnline extends Tag {

    public TagIsOnline(int idTag, String isOnline){
        this.idTag = idTag;
        this.tag = isOnline;
        this.typeOfTag = "Online";
    }
}
