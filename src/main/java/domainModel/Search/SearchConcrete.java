package domainModel.Search;

import dao.LessonDAO;
import domainModel.Lesson;

import java.util.ArrayList;
import java.util.List;

public class SearchConcrete implements Search {

    private final String query;

    public SearchConcrete(){
        this.query = "SELECT * FROM lessons JOIN lessonsTags ON lessons.idLesson = lessonsTags.idLesson WHERE lessons.state = 'Available'";
    }

    @Override
    public String getSearchQuery() {
        return query;
    }

}
