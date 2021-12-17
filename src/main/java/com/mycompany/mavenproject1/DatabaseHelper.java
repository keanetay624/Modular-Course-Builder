/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Keane Local
 */
public class DatabaseHelper {
    
    public static void archiveCourse(Course selectedCourse) throws SQLException {
        Database.openConnection();
        Statement st = Database.getSharedConnection().createStatement();

        String setArchiveTrue = "Update Course set is_archived = 1"
                + " where course_code = '" + selectedCourse.getCourseCode() + "';";
        System.out.println("query for archival: " + setArchiveTrue);
        st.execute(setArchiveTrue);
        
        st.close();
        Database.closeConnection();
    }
    
    public static void insertIntoCourse(Course selectedCourse) throws SQLException {
        Database.openConnection();
        Statement st = Database.getSharedConnection().createStatement();

        //insert statement for new course
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("INSERT INTO "
                + "course (course_code, course_name, faculty_name, school_name, level, campus, is_archived) "
                + "VALUES (?,?,?,?,?,?,?)");
        pst.setString(1, selectedCourse.getCourseCode());
        pst.setString(2, selectedCourse.getName());
        pst.setString(3, selectedCourse.getFaculty());
        pst.setString(4, selectedCourse.getSchool());
        pst.setInt(5,0);
        pst.setString(6, selectedCourse.getCampus());
        pst.setInt(7, 0);

        pst.executeUpdate();
        
        st.close();
        Database.closeConnection();
    }
}
