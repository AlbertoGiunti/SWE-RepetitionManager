package dao;

import domainModel.Lesson;
import domainModel.State.*;
import domainModel.Tags.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SQLiteLessonDAO implements LessonDAO{

    // Get a specific lesson
    @Override
    public Lesson get(Integer id) throws SQLException {
        Connection con = Database.getConnection();
        Lesson lesson = null;
        PreparedStatement ps = con.prepareStatement("SELECT * FROM lessons WHERE id = ?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if(rs.next()){
            // This recreates the attributes of the lesson
            lesson = new Lesson(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    LocalDateTime.parse(rs.getString("startTime")),
                    LocalDateTime.parse(rs.getString("endTime")),
                    rs.getDouble("price"),
                    rs.getString("tutorCF")
            );

            // This recreates the state of the lesson
            if (Objects.equals(rs.getString("state"), "Booked")){
                String a = rs.getString("stateExtraInfo");
                Booked booked = new Booked(a);
                lesson.setState(booked);
            } else if (Objects.equals(rs.getString("state"), "Cancelled")) {
                LocalDateTime ldt = LocalDateTime.parse(rs.getString("stateExtraInfo"));
                Cancelled cancelled = new Cancelled(ldt);
                lesson.setState(cancelled);
            } else if (Objects.equals(rs.getString("state"), "Completed")) {
                LocalDateTime ldt = LocalDateTime.parse(rs.getString("stateExtraInfo"));
                Completed completed = new Completed(ldt);
                lesson.setState(completed);
            }else {
                Available available = new Available();
                lesson.setState(available);
            }

            // This recreates the tags of the lesson
            List<Tag> lessonTags = getTagsByLesson(id);
            for (Tag t: lessonTags){
                lesson.addTag(t);
            }

        }


        rs.close();
        ps.close();

        Database.closeConnection(con);
        return lesson;
    }

    private List<Tag> getTagsByLesson(Integer idLesson) throws SQLException{
        Connection con = Database.getConnection();
        List<Tag> tags = new ArrayList<>();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM lessonsTags JOIN tags ON lessonsTags.idTag = tags.idTag WHERE idLesson = ?");
        ps.setInt(1, idLesson);
        ResultSet rs = ps.executeQuery();

        // This recreates the tags of the lesson, is inside the if because if the lesson is null, you have no tags
        while (rs.next()){
            if(Objects.equals(rs.getString("tagType"), "Online")){
                TagIsOnline tio = new TagIsOnline(rs.getString("tag"));
                tags.add(tio);
            } else if (Objects.equals(rs.getString("tagType"), "Level")) {
                TagLevel tl = new TagLevel(rs.getString("tag"));
                tags.add(tl);
            } else if (Objects.equals(rs.getString("tagType"), "Subject")) {
                TagSubject ts = new TagSubject(rs.getString("tag"));
                tags.add(ts);
            } else if (Objects.equals(rs.getString("tagType"), "Zone")) {
                TagZone tz = new TagZone(rs.getString("tag"));
                tags.add(tz);
            }
        }

        return tags;
    }

    // Get all lessons
    @Override
    public List<Lesson> getAll() throws SQLException{
        Connection con = Database.getConnection();
        List<Lesson> lessons = new ArrayList<>();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM lessons");

        while (rs.next()){
            Lesson lesson = new Lesson(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    LocalDateTime.parse(rs.getString("startTime")),
                    LocalDateTime.parse(rs.getString("endTime")),
                    rs.getDouble("price"),
                    rs.getString("tutorCF")
            );

            // This recreates the state of the lesson
            if (Objects.equals(rs.getString("state"), "Booked")){
                String a = rs.getString("stateExtraInfo");
                Booked booked = new Booked(a);
                lesson.setState(booked);
            } else if (Objects.equals(rs.getString("state"), "Cancelled")) {
                LocalDateTime ldt = LocalDateTime.parse(rs.getString("stateExtraInfo"));
                Cancelled cancelled = new Cancelled(ldt);
                lesson.setState(cancelled);
            } else if (Objects.equals(rs.getString("state"), "Completed")) {
                LocalDateTime ldt = LocalDateTime.parse(rs.getString("stateExtraInfo"));
                Completed completed = new Completed(ldt);
                lesson.setState(completed);
            }else {
                Available available = new Available();
                lesson.setState(available);
            }

            // This recreates the tags of the lesson
            List<Tag> lessonTags = getTagsByLesson(rs.getInt("id"));
            for (Tag t: lessonTags){
                lesson.addTag(t);
            }

            lessons.add(lesson);
        }
        rs.close();
        stmt.close();
        Database.closeConnection(con);
        return lessons;
    }

    // Insert method

    @Override
    public void insert(Lesson lesson) throws SQLException {
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement("INSERT INTO lessons (title, description, startTime, endTime, price, tutorCF) VALUES (?, ?, ?, ?, ?, ?)");
        // id is not needed because is autoincremented
        ps.setString(1, lesson.getTitle());
        ps.setString(2, lesson.getDescription());
        ps.setString(3, lesson.getStartTime().toString());
        ps.setString(4, lesson.getEndTime().toString());
        ps.setDouble(5, lesson.getPrice());
        ps.setString(6, lesson.getTutorCF());

        ps.executeUpdate();

        ps.close();
        Database.closeConnection(con);
    }

    // Update method
    // TODO Solo il proprietario può modificare la lezione
    @Override
    public void update(Lesson lesson) throws SQLException {
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement(
                "UPDATE lessons SET title = ?, description = ?, startTime = ?, endTime = ?, price = ? WHERE id =?"); // Non ho messo tutorCF, non ha senso
        ps.setString(1, lesson.getTitle());
        ps.setString(2, lesson.getDescription());
        ps.setString(3, lesson.getStartTime().toString());
        ps.setString(4, lesson.getEndTime().toString());
        ps.setDouble(5, lesson.getPrice());
        ps.setInt(6, lesson.getId());

        ps.close();
        Database.closeConnection(con);
    }

    // Delete method
    @Override
    public boolean delete(Integer id) throws SQLException {
        // Usa il metodo get scritto prima
        Lesson lesson = get(id);
        if (lesson == null) {
            return false;
        }
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement("DELETE FROM lessons WHERE id = ?");
        ps.setInt(1, id);
        int rows = ps.executeUpdate();

        ps.close();
        Database.closeConnection(con);
        return rows > 0;
    }

    @Override
    // Get all the lessons created by a Tutor
    public List<Lesson> getTutorLessons(String tCF) throws SQLException {
        Connection con = Database.getConnection();
        //TODO riguarda questa query
        PreparedStatement ps = con.prepareStatement("SELECT * FROM advertisements LEFT JOIN lessons ON advertisements.id == lessons.adID WHERE tutorCF = ? AND lessons.studentCF == null");
        ps.setString(1, tCF);
        ResultSet rs = ps.executeQuery();

        List<Lesson> advertisements = new ArrayList<>();
        while (rs.next()) {
            advertisements.add(this.get(rs.getInt("id"))); // TODO riguardare
        }

        rs.close();
        ps.close();
        Database.closeConnection(con);
        return advertisements;
    }

    @Override
    public int getLastAdID() throws SQLException{
        Connection connection = Database.getConnection();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT MAX(id) FROM main.advertisements");
        int id = rs.getInt(1) + 1;

        rs.close();
        stmt.close();
        Database.closeConnection(connection);
        return id;
    }

    //TODO changeState

}
