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

    private final TagDAO tagDAO;

    public SQLiteLessonDAO(TagDAO tagDAO){
        this.tagDAO = tagDAO;
    }

    // This recreates the lesson state
    private void setLessonState(ResultSet rs, Lesson lesson) throws SQLException {
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
    }



    // Get a specific lesson
    @Override
    public Lesson get(Integer id) throws Exception {
        Connection con = Database.getConnection();
        Lesson lesson = null;
        PreparedStatement ps = con.prepareStatement("SELECT * FROM lessons WHERE idLesson = ?");
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
            this.setLessonState(rs, lesson);

            // This recreates the tags of the lesson
            List<Tag> lessonTags = this.tagDAO.getTagsByLesson(lesson.getIdLesson());
            for (Tag t: lessonTags){
                lesson.addTag(t);
            }

        }

        rs.close();
        ps.close();

        Database.closeConnection(con);
        return lesson;
    }

    // Get all lessons
    @Override
    public List<Lesson> getAll() throws Exception {
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
            this.setLessonState(rs, lesson);

            // This recreates the tags of the lesson
            List<Tag> lessonTags = this.tagDAO.getTagsByLesson(lesson.getIdLesson());
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
    public void insert(Lesson lesson) throws Exception {
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement("INSERT INTO lessons (title, description, startTime, endTime, price, tutorCF, state, stateExtraInfo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        // id is not needed because is autoincrement
        ps.setString(1, lesson.getTitle());
        ps.setString(2, lesson.getDescription());
        ps.setString(3, lesson.getStartTime().toString());
        ps.setString(4, lesson.getEndTime().toString());
        ps.setDouble(5, lesson.getPrice());
        ps.setString(6, lesson.getTutorCF());
        ps.setString(7, lesson.getState());
        ps.setString(8, lesson.getStateExtraInfo());

        ps.executeUpdate();

        ps.close();

        for (Tag t : lesson.getTags()){
            // Add the tag to the DB if it does not exist
            if (this.tagDAO.getTag(t.getTag(), t.getTypeOfTag()) == null){
                this.tagDAO.addTag(t);
            }
            // Attach the tag at the lesson
            this.tagDAO.attachTag(this.getNextLessonID(), t);
        }

        Database.closeConnection(con);
    }


    // Update method
    // TODO Solo il proprietario può modificare la lezione
    @Override
    public void update(Lesson lesson) throws SQLException {
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement(
                "UPDATE lessons SET title = ?, description = ?, startTime = ?, endTime = ?, price = ? WHERE idLesson =?"); // Non ho messo tutorCF, non ha senso
        ps.setString(1, lesson.getTitle());
        ps.setString(2, lesson.getDescription());
        ps.setString(3, lesson.getStartTime().toString());
        ps.setString(4, lesson.getEndTime().toString());
        ps.setDouble(5, lesson.getPrice());
        ps.setInt(6, lesson.getIdLesson());

        ps.close();

        Database.closeConnection(con);
    }

    // Delete method
    @Override
    public boolean delete(Integer idLesson) throws Exception {
        // Usa il metodo get scritto prima
        Lesson lesson = get(idLesson);
        if (lesson == null) {
            return false;
        }
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement("DELETE FROM lessons WHERE idLesson = ?");
        ps.setInt(1, idLesson);

        int rows = ps.executeUpdate();

        ps.close();
        Database.closeConnection(con);
        return rows > 0;
    }

    @Override
    // Get all the lessons created by a Tutor
    public List<Lesson> getTutorLessonsByState(String tCF, State state) throws Exception {
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM lessons WHERE tutorCF = ? AND state = ?");
        ps.setString(1, tCF);
        ps.setString(2, state.getState());
        ResultSet rs = ps.executeQuery();

        List<Lesson> lessons = new ArrayList<>();
        while (rs.next()) {
            lessons.add(this.get(rs.getInt("idLesson")));
        }

        rs.close();
        ps.close();
        Database.closeConnection(con);
        return lessons;
    }

    public List<Lesson> getStudentBookedLessons(String sCF) throws Exception {
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM lessons WHERE state = ? AND stateExtraInfo = ?");
        ps.setString(1, "Booked");
        ps.setString(2, sCF);
        ResultSet rs = ps.executeQuery();

        List<Lesson> lessons = new ArrayList<>();
        while (rs.next()){
            lessons.add(this.get(rs.getInt("idLesson")));
        }

        rs.close();
        ps.close();
        Database.closeConnection(con);
        return lessons;
    }

    @Override
    public int getNextLessonID() throws SQLException{
        Connection connection = Database.getConnection();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT MAX(idLesson) FROM main.lessons");
        int id = rs.getInt(1) + 1;

        rs.close();
        stmt.close();
        Database.closeConnection(connection);
        return id;
    }


    // This method change state
    @Override
    public void changeState(Integer idLesson, State newState) throws SQLException{
        Connection con = Database.getConnection();
        PreparedStatement ps =con.prepareStatement("UPDATE lessons SET state = ?, stateExtraInfo = ? WHERE idLesson = ?");
        ps.setString(1, newState.getState());
        ps.setString(2, newState.getExtraInfo());
        ps.setInt(3, idLesson);

        ps.executeUpdate();

        ps.close();
        Database.closeConnection(con);
    }

}
