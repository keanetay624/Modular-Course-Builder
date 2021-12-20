/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Keane Local
 */
public class DatabaseHelper {
    
    /**
    * Helper functions for Course 
    */
    
    public static void archiveCourse(Course selectedCourse) throws SQLException {
        Database.openConnection();

        PreparedStatement pst = Database.getSharedConnection().prepareStatement("UPDATE Course "
                + "set is_archived = 1 where course_code = ?");
        pst.setString(1, selectedCourse.getCourseCode());
        
        pst.executeUpdate();
        pst.close();
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
    
    public static ObservableList<Course> getCourses() throws SQLException {

        Database.openConnection();
        Statement st = Database.getSharedConnection().createStatement();

        String query = "Select * from course where is_archived = '0'";

        ResultSet rs = st.executeQuery(query);

        // Create and return a new ObservableList with each call.
        ObservableList<Course> newList = FXCollections.observableArrayList();

        while (rs.next()) {
            newList.add(new Course(rs.getString(1), 
                    rs.getString(2), rs.getString(3), rs.getString(4),
                    rs.getInt(5), rs.getString(6), rs.getInt(7)));
        }
        
        st.close();
        Database.closeConnection();
        return newList;
    }
    
    /**
    * Helper functions for Module
    */

    public static void archiveModule(Module selectedModule) throws SQLException {
        Database.openConnection();
        
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("UPDATE module "
                + "set is_archived = 1 where module_name = ?");
        pst.setString(1, selectedModule.getName());
        
        pst.executeUpdate();
        pst.close();
        Database.closeConnection();
    }
    
    public static void insertIntoModule(Module selectedModule) throws SQLException {
        Database.openConnection();

        //insert statement for new course
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("INSERT INTO "
                + "module (module_name, module_description, is_archived) "
                + "VALUES (?,?,?)");
        pst.setString(1, selectedModule.getName());
        pst.setString(2, selectedModule.getDesc());
        pst.setInt(3, 0);

        pst.executeUpdate();
        pst.close();
        Database.closeConnection();
    }
    
    public static ObservableList<Module> getModules() throws SQLException {

        Database.openConnection();
        Statement st = Database.getSharedConnection().createStatement();

        String query = "Select * from module where is_archived = '0'";

        ResultSet rs = st.executeQuery(query);

        //create new list to write over original observable list
        ObservableList<Module> newList = FXCollections.observableArrayList();

        while (rs.next()) {
            newList.add(new Module(
                    rs.getString(1), rs.getString(2), rs.getInt(3)));
        }
        
        st.close();
        Database.closeConnection();
        
        return newList;
    }
    
    /**
    * Helper functions for Section
    */

    public static void archiveSection(Section selectedSection) throws SQLException {
        Database.openConnection();
        
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("UPDATE section "
                + "set is_archived = 1 where section_id = ?");
        pst.setString(1, selectedSection.getId());
        
        pst.executeUpdate();
        pst.close();
        Database.closeConnection();
    }
    
    public static void insertIntoSection(Section selectedSection) throws SQLException {
        Database.openConnection();

        //insert statement for new course
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("INSERT INTO "
                + "section (section_id, module_name, sequence, name, desc, is_archived) "
                + "VALUES (?,?,?)");
        pst.setString(1, selectedSection.getId());
        pst.setString(2, selectedSection.getsModule().getName());
        pst.setInt(3, selectedSection.getSequence());
        pst.setString(4, selectedSection.getName());
        pst.setString(5, selectedSection.getDesc());
        pst.setInt(6, selectedSection.getIsArchived());

        pst.executeUpdate();
        pst.close();
        Database.closeConnection();
    }
    
    public static ObservableList<Section> getSections() throws SQLException {

        Database.openConnection();
        Statement st = Database.getSharedConnection().createStatement();

        String query = "Select * from section where is_archived = '0'";

        ResultSet rs = st.executeQuery(query);

        //create new list to write over original observable list
        ObservableList<Section> newList = FXCollections.observableArrayList();

        while (rs.next()) {
            newList.add(new Section(
                    rs.getString(1), new Module(rs.getString(2),"",0),  rs.getString(3), rs.getString(4), rs.getInt(5), rs.getInt(6)));
        }
        
        st.close();
        Database.closeConnection();
        
        return newList;
    }
    
    /**
    * Helper functions for Resource
    */

//    public static void archiveModule(Module selectedModule) throws SQLException {
//        Database.openConnection();
//        
//        PreparedStatement pst = Database.getSharedConnection().prepareStatement("UPDATE module "
//                + "set is_archived = 1 where module_name = ?");
//        pst.setString(1, selectedModule.getName());
//        
//        pst.executeUpdate();
//        pst.close();
//        Database.closeConnection();
//    }
//    
//    public static void insertIntoModule(Module selectedModule) throws SQLException {
//        Database.openConnection();
//
//        //insert statement for new course
//        PreparedStatement pst = Database.getSharedConnection().prepareStatement("INSERT INTO "
//                + "module (module_name, module_description, is_archived) "
//                + "VALUES (?,?,?)");
//        pst.setString(1, selectedModule.getName());
//        pst.setString(2, selectedModule.getDesc());
//        pst.setInt(3, 0);
//
//        pst.executeUpdate();
//        pst.close();
//        Database.closeConnection();
//    }
//    
//    public static ObservableList<Module> getModules() throws SQLException {
//
//        Database.openConnection();
//        Statement st = Database.getSharedConnection().createStatement();
//
//        String query = "Select * from module where is_archived = '0'";
//
//        ResultSet rs = st.executeQuery(query);
//
//        //create new list to write over original observable list
//        ObservableList<Module> newList = FXCollections.observableArrayList();
//
//        while (rs.next()) {
//            newList.add(new Module(
//                    rs.getString(1), rs.getString(2), rs.getInt(3)));
//        }
//        
//        st.close();
//        Database.closeConnection();
//        
//        return newList;
//    }
}
