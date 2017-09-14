package com.example.demo.Interfaces;

import com.example.demo.Domain.Errand;
import com.example.demo.Domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.sql.DataSource;
import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcRepository implements ErrandRepository {

    @Autowired
    DataSource dataSource;

    @Override
    public void addErrand(String owner, String topic, String errand, String created) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO Errands(owner, topic, errand, status, created) VALUES (?,?,?,?,?)")) {
            ps.setString(1, owner);
            ps.setString(2, topic);
            ps.setString(3, errand);
            ps.setString(4, "Väntar");
            ps.setString(5, created);
            ps.executeUpdate();
        } catch (SQLException e) {}
    }

    @Override
    public void deleteErrand(long errandId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Errands WHERE errandid = ?")) {
            ps.setLong(1,errandId);
            ps.executeUpdate();
        } catch (SQLException e) {}
    }

    @Override
    public void fileErrand (long errandId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE Errands SET status = 'Arkiverad' WHERE errandid = ?")) {
            ps.setLong(1, errandId);
            ps.executeUpdate();
        } catch (SQLException e) {}
    }

    @Override
    public void chooseErrand(long errandId, String name) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE Errands SET status = 'Under behandling', administrator=? WHERE errandid=?")) {
            ps.setString(1, name);
            ps.setLong(2,errandId);
            ps.executeUpdate();
        } catch (SQLException e) {
            //throw new QueueRepositoryException(e);
        }
    }

    @Override
    public void reactivateErrand(long errandId) {
        try (Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("UPDATE Errands SET status = 'Väntar' WHERE errandid=?")) {
            ps.setLong(1, errandId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addUser(String name, String username, String password, String admin) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO Users(fullname, username, password, admin) VALUES (?,?,?,?)")) {
            ps.setString(1, name);
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setString(4, admin);
            ps.executeUpdate();
        } catch (SQLException e) {
        }
    }

    @Override
    public List<Errand> getErrands(){
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT errandid, owner, topic, errand, status, administrator, created FROM errands WHERE status = 'Väntar' OR status = 'Under behandling'")) {
            List<Errand> errands = new ArrayList<>();
            while (rs.next()) errands.add(rsErrands(rs));
            return errands;
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Errand getErrand(long errandId){
        try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT errandid, owner, topic, errand, status, administrator, created FROM errands WHERE errandid=?")) {
            ps.setLong(1, errandId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rsErrands(rs);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List <Errand> getFiledErrands (){
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT errandid, owner, topic, errand, status, administrator, created FROM errands WHERE status = 'Arkiverad'")) {
            List <Errand> filedErrands = new ArrayList<>();
            while (rs.next()) filedErrands.add(rsFiledErrands(rs));
            return filedErrands;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public User verifyUser(String username, String password){
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT userid, fullname, username, password, admin FROM Users WHERE username =? and password=?")){
             ps.setString(1, username);
             ps.setString(2, password);
             ResultSet rs = ps.executeQuery();
             if (rs.next()){
                 return rsUsers(rs);
             }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    private Errand rsErrands (ResultSet rs) throws SQLException {
        return new Errand(rs.getLong("errandid"), rs.getString("owner"), rs.getString("topic"), rs.getString("errand"), rs.getString("status"), rs.getString("administrator"), rs.getString("created"));
    }

    private User rsUsers(ResultSet rs) throws SQLException {
        return new User(rs.getLong("userid"), rs.getString("fullname"), rs.getString("username"), rs.getString("password"), rs.getString("admin"));
    }

    private Errand rsFiledErrands (ResultSet rs) throws SQLException {
        return new Errand(rs.getLong("errandid"), rs.getString("owner"), rs.getString("topic"), rs.getString("errand"), rs.getString("status"), rs.getString("administrator"), rs.getString("created"));
    }
}
