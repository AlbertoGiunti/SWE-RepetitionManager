package dao;

import domainModel.Lesson;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SQLiteLessonDAO implements LessonDAO{

    //TODO MANCA STATO E TAGS!!

    // Get a specific lesson
    @Override
    public Lesson get(Integer id) throws SQLException {
        Connection con = Database.getConnection();
        Lesson lesson = null;
        PreparedStatement ps = con.prepareStatement("SELECT * FROM lessons WHERE id = ?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if(rs.next()){
            lesson = new Lesson(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    LocalDateTime.parse(rs.getString("startTime")),
                    LocalDateTime.parse(rs.getString("endTime")),
                    rs.getDouble("price"),
                    rs.getString("tutorCF")
            );

        }

        rs.close();
        ps.close();

        Database.closeConnection(con);
        return lesson;
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


}
