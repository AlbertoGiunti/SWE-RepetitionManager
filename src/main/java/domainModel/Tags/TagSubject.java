package domainModel.Tags;

public class TagSubject extends Tag {

    public TagSubject(int idTag, String subject){
        this.idTag = idTag;
        this.tag = subject;
        this.typeOfTag = "Subject";
    }
}
