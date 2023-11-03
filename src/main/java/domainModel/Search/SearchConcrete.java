package domainModel.Search;

import dao.LessonDAO;
import domainModel.Lesson;

import java.util.ArrayList;
import java.util.List;

public class SearchConcrete implements Search {

    private final String query;

    public SearchConcrete(){
        this.query = "SELECT * FROM lessons AS L WHERE L.state='Available'";
    }

    @Override
    public String getSearchQuery() {
        return query;
    }

}
