/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Keane Local
 */
public class CourseHelper {
    private static ObservableList<Course> courseObservableList = FXCollections.observableArrayList();
    
    public static ObservableList<Course> getCourseObservableList() {
        return courseObservableList;
    }

    public static void setCourseObservableList(ObservableList<Course> courseObservableList) {
        CourseHelper.courseObservableList = courseObservableList;
    }
    
    public static void getCourses() throws SQLException {

        Database.openConnection();
        Statement st = Database.getSharedConnection().createStatement();

        String query = "Select * from course where is_archived = '0'";

        ResultSet rs = st.executeQuery(query);

        //create new list to write over original observable list
        ObservableList<Course> newList = FXCollections.observableArrayList();
        CourseHelper.setCourseObservableList(newList);

        while (rs.next()) {
            courseObservableList.add(new Course(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getString(6), rs.getInt(7)));
        }
        
        st.close();
        Database.closeConnection();
    }
}
