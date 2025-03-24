package com.project.parsing.db;

import com.project.parsing.model.Unit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class WriteToDB {
    private static final String URL = "jdbc:postgresql://localhost:5432/labResult";
    private static final String USER = "postgres";
    private static final String PASSWORD = "212030";
    public void saveUnitsToDatabase(List<Unit> units){
        String sql = "INSERT INTO jobs (jobname,aboutjob,salary) VALUES(?, ?, ?)";
        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement preparedStatement = conn.prepareStatement(sql)){
            for(Unit job:units){
                preparedStatement.setString(1,job.getJobName());
                preparedStatement.setString(2,job.getAboutJob());
                preparedStatement.setString(3,job.getJobSalaryRange());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            System.out.println("Запис виконано успішно!");
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
}
