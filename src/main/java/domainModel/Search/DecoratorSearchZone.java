package domainModel.Search;

public class DecoratorSearchZone extends BaseDecoratorSearch {

    private final String zone;

    public DecoratorSearchZone(Search decoratedSearch, String zone){
        super(decoratedSearch);
        this.zone = zone;
    }

    @Override
    public String getSearchQuery() {
        return super.getSearchQuery() + " AND L.idLesson IN (SELECT idLesson FROM lessonsTags WHERE tagType = 'Zone' AND tag = '" + zone + "' )";
    }

}
