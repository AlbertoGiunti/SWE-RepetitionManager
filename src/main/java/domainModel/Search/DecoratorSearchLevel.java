package domainModel.Search;

public class DecoratorSearchLevel extends BaseDecoratorSearch{
    private final String level;

    public DecoratorSearchLevel(Search decoratedSearch, String level){
        super(decoratedSearch);
        this.level = level;
    }

    @Override
    public String getSearchQuery() {
        return super.getSearchQuery() + " AND L.idLesson IN (SELECT idLesson FROM lessonsTags WHERE tagType = 'Level' AND tag = '" + level + "' )";
    }
}
