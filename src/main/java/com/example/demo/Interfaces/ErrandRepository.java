package com.example.demo.Interfaces;

import com.example.demo.Domain.Errand;

import java.util.List;
import javax.sql.DataSource;

public interface ErrandRepository {
    void addErrand(String name, String topic, String errand);
    List <Errand> getErrands();
    void deleteErrand(int errandId);
    void chooseErrand(int errandId);
    void addUser(String name, String username, String password);
    boolean verifyUser(String username, String password);

    void fileErrand (int errandId);
    List <Errand> getFiledErrands();
}
