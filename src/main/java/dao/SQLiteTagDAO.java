package dao;

import domainModel.Lesson;
import domainModel.Tags.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SQLiteTagDAO implements TagDAO{
    @Override
    public Tag getTag(Integer idTag) throws SQLException {
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM tags WHERE idTag = ?");
        ps.setInt(1, idTag);
        ResultSet rs = ps.executeQuery();

        if (rs.next()){
            if(Objects.equals(rs.getString("tagType"), "Online")){
                TagIsOnline tio = new TagIsOnline(rs.getInt("idTag"), rs.getString("tag"));
                rs.close();
                ps.close();
                Database.closeConnection(con);
                return tio;
            } else if (Objects.equals(rs.getString("tagType"), "Level")) {
                TagLevel tl = new TagLevel(rs.getInt("idTag"), rs.getString("tag"));
                rs.close();
                ps.close();
                Database.closeConnection(con);
                return tl;
            } else if (Objects.equals(rs.getString("tagType"), "Subject")) {
                TagSubject ts = new TagSubject(rs.getInt("idTag"), rs.getString("tag"));
                rs.close();
                ps.close();
                Database.closeConnection(con);
                return ts;
            } else if (Objects.equals(rs.getString("tagType"), "Zone")) {
                TagZone tz = new TagZone(rs.getInt("idTag"), rs.getString("tag"));
                rs.close();
                ps.close();
                Database.closeConnection(con);
                return tz;
            }
        }

        rs.close();
        ps.close();
        Database.closeConnection(con);

        return null;
    }

    @Override
    public List<Tag> getAllTags() throws SQLException {
        Connection con = Database.getConnection();
        List<Tag> tags = new ArrayList<>();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM tags");

        while (rs.next()){
            if(Objects.equals(rs.getString("tagType"), "Online")){
                TagIsOnline tio = new TagIsOnline(rs.getInt("idTag"), rs.getString("tag"));
                tags.add(tio);
            } else if (Objects.equals(rs.getString("tagType"), "Level")) {
                TagLevel tl = new TagLevel(rs.getInt("idTag"), rs.getString("tag"));
                tags.add(tl);
            } else if (Objects.equals(rs.getString("tagType"), "Subject")) {
                TagSubject ts = new TagSubject(rs.getInt("idTag"), rs.getString("tag"));
                tags.add(ts);
            } else if (Objects.equals(rs.getString("tagType"), "Zone")) {
                TagZone tz = new TagZone(rs.getInt("idTag"), rs.getString("tag"));
                tags.add(tz);
            }
        }

        rs.close();
        stmt.close();
        Database.closeConnection(con);
        return tags;
    }

    @Override
    public void addTag(Lesson lesson, Tag tag) throws SQLException {
        Connection con = Database.getConnection();
        PreparedStatement ps1 = con.prepareStatement("INSERT INTO tags (tag, tagType) VALUES (?, ?)");
        ps1.setString(1, tag.getTag());
        ps1.setString(2, tag.getTypeOfTag());
        ps1.executeUpdate();
        ps1.close();

        PreparedStatement ps2 = con.prepareStatement("INSERT INTO lessonsTags (idTag, idLesson) VALUES (?, ?)");
        ps2.setInt(1, lesson.getIdLesson());
        ps2.setInt(2, this.getLastTagID());
        ps2.executeUpdate();
        ps2.close();


        Database.closeConnection(con);
    }

    @Override
    public boolean removeTag(Integer idTag) throws SQLException {
        // Usa il metodo get scritto prima
        Tag tag = getTag(idTag);
        if (tag == null) {
            return false;
        }
        Connection con = Database.getConnection();
        PreparedStatement ps0 = con.prepareStatement("DELETE FROM lessonsTags WHERE idTag = ?");
        ps0.setInt(1, idTag);
        ps0.executeUpdate();
        ps0.close();

        PreparedStatement ps = con.prepareStatement("DELETE FROM tags WHERE idTag = ?");
        ps.setInt(1, idTag);
        int rows = ps.executeUpdate();

        ps.close();
        Database.closeConnection(con);
        return rows > 0;
    }


    @Override
    // This recreates the tags of a lesson
    public List<Tag> getTagsByLesson(Integer idLesson) throws SQLException {
        Connection con = Database.getConnection();
        List<Tag> tags = new ArrayList<>();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM lessonsTags JOIN tags ON lessonsTags.idTag = tags.idTag WHERE idLesson = ?");
        ps.setInt(1, idLesson);
        ResultSet rs = ps.executeQuery();

        // This recreates the tags of the lesson, is inside the if because if the lesson is null, you have no tags
        while (rs.next()){
            if(Objects.equals(rs.getString("tagType"), "Online")){
                TagIsOnline tio = new TagIsOnline(rs.getInt("idTag"), rs.getString("tag"));
                tags.add(tio);
            } else if (Objects.equals(rs.getString("tagType"), "Level")) {
                TagLevel tl = new TagLevel(rs.getInt("idTag"), rs.getString("tag"));
                tags.add(tl);
            } else if (Objects.equals(rs.getString("tagType"), "Subject")) {
                TagSubject ts = new TagSubject(rs.getInt("idTag"), rs.getString("tag"));
                tags.add(ts);
            } else if (Objects.equals(rs.getString("tagType"), "Zone")) {
                TagZone tz = new TagZone(rs.getInt("idTag"), rs.getString("tag"));
                tags.add(tz);
            }
        }

        return tags;
    }

    public int getLastTagID() throws SQLException{
        Connection connection = Database.getConnection();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT MAX(idTag) FROM main.tags");
        int id = rs.getInt(1) + 1;

        rs.close();
        stmt.close();
        Database.closeConnection(connection);
        return id;
    }


}
