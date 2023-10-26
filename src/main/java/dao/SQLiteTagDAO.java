package dao;

import domainModel.Tags.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SQLiteTagDAO implements TagDAO{
    @Override
    public Tag getTagByID(Integer idTag) throws SQLException {
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
    public Tag getTagByInfo(String tag, String tagType) throws SQLException {
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM tags WHERE tag = ? AND tagType = ?");
        ps.setString(1, tag);
        ps.setString(2, tagType);
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
    public void addTag(Tag tag) throws SQLException {
        Connection con = Database.getConnection();
        PreparedStatement ps1 = con.prepareStatement("INSERT INTO tags (tag, tagType) VALUES (?, ?)");
        ps1.setString(1, tag.getTag());
        ps1.setString(2, tag.getTypeOfTag());
        ps1.executeUpdate();
        ps1.close();

        Database.closeConnection(con);
    }

    @Override
    public void attachTag(Integer idTag, Integer idLesson) throws SQLException {

        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement("INSERT INTO lessonsTags (idTag, idLesson) VALUES (?, ?)");
        ps.setInt(1, idLesson);
        ps.setInt(2, this.getNextTagID());
        ps.executeUpdate();
        ps.close();

        con.close();

    }

    @Override
    public boolean removeTag(Integer idTag) throws SQLException {

        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement("DELETE FROM tags WHERE idTag = ?");
        ps.setInt(1, idTag);
        int rows = ps.executeUpdate();

        ps.close();
        Database.closeConnection(con);
        return rows > 0;
    }

    public void detachTag(Integer idTag, Integer idLesson) throws SQLException {
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement("DELETE FROM lessonsTags WHERE idTag = ? AND idLesson = ?");
        ps.setInt(1, idTag);
        ps.setInt(2, idLesson);
        ps.executeUpdate();
        ps.close();

        con.close();
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

    public int getNextTagID() throws SQLException{
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
