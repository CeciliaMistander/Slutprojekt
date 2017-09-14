package com.example.demo.Interfaces;

import com.example.demo.Domain.Errand;
import com.example.demo.Domain.User;

import java.util.List;
import javax.sql.DataSource;

public interface ErrandRepository {
    void addErrand(String owner, String topic, String errand, String date);
    List <Errand> getErrands();
    Errand getErrand(long errandId);
    void deleteErrand(long errandId);
    void chooseErrand(long errandId, String name);
    void addUser(String name, String username, String password, String admin);
    User verifyUser(String username, String password);
    void reactivateErrand(long errandId);
    void fileErrand (long errandId);
    List <Errand> getFiledErrands();
}
