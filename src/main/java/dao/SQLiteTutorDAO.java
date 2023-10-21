package dao;

import domainModel.Tutor;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
public class SQLiteTutorDAO implements TutorDAO {

    //Get a specific tutor
    @Override
    public Tutor get(String CF) throws SQLException{
        // Create a connection with the DB
        Connection con = Database.getConnection();
        Tutor tutor = null;
        // Questa riga prepara una dichiarazione SQL parametrizzata per selezionare i dati dei trainer dal database in base al codice fiscale
        PreparedStatement ps = con.prepareStatement("SELECT * FROM tutors WHERE cf = ?"); // ? = placeholder
        // Questa riga imposta il valore del primo segnaposto
        ps.setString(1, CF);
        ResultSet rs = ps.executeQuery();

        // Se c'è un risultato
        if (rs.next()){
            tutor = new Tutor(
                    rs.getString("cf"),
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getString("iban")
            );
        }

        rs.close();
        ps.close();
        Database.closeConnection(con);
        return tutor;
    }

    //Get all tutors
    @Override
    public List<Tutor> getAll() throws SQLException{
        Connection con = Database.getConnection();
        List<Tutor> tutors = new ArrayList<>();
        // Statement -- used to send basic SQL statements
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM tutors");

        // Finché ci sono altri risultati continua
        while (rs.next()){
            tutors.add(new Tutor(
                    rs.getString("cf"),
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getString("iban")
            ));
        }

        return tutors;
    }

    //Insert method
    public void insert(Tutor newTutor) throws SQLException{
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement("INSERT INTO tutors (cf, name, surname, iban) VALUES (?, ?, ?, ?)");
        ps.setString(1, newTutor.getCF());
        ps.setString(2, newTutor.getName());
        ps.setString(3, newTutor.getSurname());
        ps.setString(4, newTutor.getIban());

        ps.executeUpdate();
        ps.close();
        Database.closeConnection(con);
    }

    // Update method
    @Override
    public void update(Tutor updatedTutor) throws SQLException{
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement("UPDATE tutors SET name = ?, surname = ?, iban = ? WHERE cf = ?");
        ps.setString(1, updatedTutor.getName());
        ps.setString(2, updatedTutor.getSurname());
        ps.setString(3, updatedTutor.getIban());
        ps.setString(4, updatedTutor.getCF());

        ps.executeUpdate();
        ps.close();
        Database.closeConnection(con);
    }

    // Delete method
    @Override
    public boolean delete(String CF) throws SQLException{
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement("DELETE FROM tutors WHERE cf = ?");
        ps.setString(1, CF);
        int rows = ps.executeUpdate();
        ps.close();
        Database.closeConnection(con);
        return rows > 0;
    }

}
