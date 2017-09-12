package com.example.demo.Interfaces;

import com.example.demo.Domain.Errand;
import com.example.demo.Domain.Users;
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
    JdbcTemplate jdbcTemplate;

    @Override
    public void addErrand(String name, String topic, String errand) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO Errands(fullname, topic, errand, status) VALUES (?,?,?,?)")) {
            ps.setString(1, name);
            ps.setString(2, topic);
            ps.setString(3, errand);
            ps.setString(4, "Väntar");
            ps.executeUpdate();
        } catch (SQLException e) {
            //throw new QueueRepositoryException(e);
        }
    }

    @Override
    public void deleteErrand(int errandId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Errands WHERE errandid = ?")) {
            ps.setInt(1,errandId);
            ps.executeUpdate();
        } catch (SQLException e) {
            //throw new QueueRepositoryException(e);
        }
    }

    @Override
    public void fileErrand (int errandId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE Errands SET status = 'Arkiverad' WHERE errandid = ?")) {
            ps.setInt(1, errandId);
            ps.executeUpdate();
        } catch (SQLException e) {

        }
    }

    @Override
    public void chooseErrand(int errandId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE Errands SET status = 'Under behandling' WHERE errandid=?")) {
            ps.setInt(1,errandId);
            ps.executeUpdate();
        } catch (SQLException e) {
            //throw new QueueRepositoryException(e);
        }
    }

    @Override
    public void addUser(String name, String username, String password) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO Users(fullname, username, password) VALUES (?,?,?)")) {
            ps.setString(1, name);
            ps.setString(2, username);
            ps.setString(3, password);
            ps.executeUpdate();
        } catch (SQLException e) {
        }
    }

    @Override
    public List<Errand> getErrands(){
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT errandid, fullname, topic, errand, status FROM errands WHERE status = 'Väntar' OR status = 'Under behandling'")) {
            List<Errand> errands = new ArrayList<>();
            while (rs.next()) errands.add(rsErrands(rs));
            return errands;
        } catch (SQLException e){
            return null;
        }
    }

    @Override
    public List <Errand> getFiledErrands (){
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT errandid, fullname, topic, errand, status FROM errands WHERE status = 'Arkiverad'")) {
            List <Errand> filedErrands = new ArrayList<>();
            while (rs.next()) filedErrands.add(rsFiledErrands(rs));
            return filedErrands;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public boolean verifyUser(String username, String password){
        boolean verified=false;
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT userid, fullname, username, password FROM Users")) {
            List<Users> allUsers = new ArrayList<>();
            while (rs.next()) allUsers.add(rsUsers(rs));
            for (Users users : allUsers) {
                if ((username.equals(users.username)) && (password.equals(users.password))) {
                    verified=true;
                    break;
                }
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return verified;
    }

    private Errand rsErrands (ResultSet rs) throws SQLException {
        return new Errand(rs.getLong("errandid"), rs.getString("fullname"), rs.getString("topic"), rs.getString("errand"), rs.getString("status"));
    }

    private Users rsUsers(ResultSet rs) throws SQLException {
        return new Users(rs.getLong("userid"), rs.getString("fullname"), rs.getString("username"), rs.getString("password"));
    }

    private Errand rsFiledErrands (ResultSet rs) throws SQLException {
        return new Errand(rs.getLong("errandid"), rs.getString("fullname"), rs.getString("topic"), rs.getString("errand"), rs.getString("status"));
    }
}
