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
    
    public static ObservableList<Module> getModulesWithinCourses(Course selectedCourse) throws SQLException {

        Database.openConnection();
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("Select * "
                + "From CoursesHaveModules where course_name = ? order by sequence_no");
        
        pst.setString(1, selectedCourse.getName());

        ResultSet rs = pst.executeQuery();

        //create new list to write over original observable list
        ObservableList<Module> newList = FXCollections.observableArrayList();

        while (rs.next()) {
            newList.add(new Module(rs.getString(2),"",0));
        }
        
        pst.close();
        Database.closeConnection();
        
        return newList;
    }
    
    // This function gets number of Modules within the given course name
    public static int getModuleCount(String courseName) throws SQLException {
        int moduleCount = 0;
        
        Database.openConnection();
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("SELECT * FROM "
                + "CoursesHaveModules WHERE course_name = ?");
        pst.setString(1, courseName);
        ResultSet rs = pst.executeQuery();
        
        while (rs.next()) {
            moduleCount++;
        }
        
        pst.close();
        Database.closeConnection();
        return moduleCount;
    }
    
    // This function parses the modules within a course name, and resets 
    // available modules in ascending order, starting from 1.
    public static void sortModules(String courseName) throws SQLException {
        System.out.println("Sorting the Modules!");
        int moduleCount = getModuleCount(courseName); //TODO: Write this function
        System.out.println("Module count:" + moduleCount);
        Database.openConnection();
        
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("SELECT * FROM "
                + "CoursesHaveModules WHERE course_name = ? order by sequence_no");
        pst.setString(1, courseName);
        
        ResultSet rs = pst.executeQuery();
        
        // initialize the minimum sequence number to the sequence_no attribute
        // in first response in the ResultSet
        
        int numSorted = 1;
        
        while (rs.next()) {
            while (numSorted <= moduleCount) {
                
                PreparedStatement pst2 = Database.getSharedConnection().prepareStatement("Update CoursesHaveModules "
                    + "set sequence_no = ? WHERE chm_id = ?");
                pst2.setInt(1, numSorted);
                pst2.setInt(2, rs.getInt(1));
                pst2.executeUpdate();
                pst2.close();

                System.out.println("sequence number to change to: " + numSorted);
                System.out.println("chm id targeted: " + rs.getInt(1));
                numSorted++;
                rs.next();
            }
        }
        
        pst.close();
        Database.closeConnection();
    }
    
    // this function returns the int position of a given module and course
    public static int getModuleIntPos(String moduleName, String courseName) throws SQLException {
        
        Database.openConnection();
        System.out.println("Module Name:" + moduleName);
        System.out.println("Course Name:" + courseName);
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("Select * FROM CoursesHaveModules "
                    + " WHERE module_name = ? and course_name = ?");
        pst.setString(1, moduleName);
        pst.setString(2, courseName);
        ResultSet rs = pst.executeQuery();
        
        int pos = rs.getInt(4);
        
        pst.close();
        Database.closeConnection();
        
        return pos;
    }
    
    // this function returns number of Modules in a given Course
    public static int getNumModules(String courseName) throws SQLException {
        
        Database.openConnection();
        
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("Select * FROM CoursesHaveModules "
                    + " WHERE course_name = ?");
        pst.setString(1, courseName);
        ResultSet rs = pst.executeQuery();
        
        int numModules = 0;
        
        while (rs.next()) {
            numModules++;
        }
        
        pst.close();
        Database.closeConnection();
        
        return numModules;
    }
    
    public static void shiftModuleUp(String selectedModule, String courseName) throws SQLException {
        // get the integer position of the selected Module
        int pos = getModuleIntPos(selectedModule, courseName);
        // if position is 1, do nothing
        
        if (pos == 1) {
            return;
        } 
        // else, get the preceding position and swap 
        Database.openConnection();
        
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("Select * FROM CoursesHaveModules "
                    + " WHERE course_name = ? and sequence_no = ?");
        pst.setString(1, courseName);
        int preceding = pos-1;
        pst.setInt(2, preceding);
        
        ResultSet rs = pst.executeQuery();
        System.out.println("Debug");
        System.out.println(rs.getInt(1)); // nothing here, perhaps cant do multiple ands?
        int id = rs.getInt(1);
        System.out.println("temp id storage:" + id);
        
        // set the current position to its preceding position
        PreparedStatement pst2 = Database.getSharedConnection().prepareStatement("Update CoursesHaveModules "
                    + " set sequence_no = ? where sequence_no = ? and course_name = ?");
        pst2.setInt(1, pos-1);
        pst2.setInt(2, pos);
        pst2.setString(3, courseName);
        pst2.executeUpdate();
        pst2.close();
        
        // set the preceding position to the currently selected position
        PreparedStatement pst3 = Database.getSharedConnection().prepareStatement("Update CoursesHaveModules "
                    + " set sequence_no = ? where chm_id = ?");
        pst3.setInt(1, pos);
        pst3.setInt(2, id);
        System.out.println("pos" + pos);
        pst3.executeUpdate();
        pst3.close();
        
        Database.closeConnection();
    }
    
    public static void shiftModuleDown(String selectedModule, String courseName) throws SQLException {
        // get the integer position of the selected Module
        int pos = getModuleIntPos(selectedModule, courseName); 
        int max = getNumModules(courseName); 
        // if position is max, do nothing
        System.out.println("pos: " + pos);
        System.out.println("Max: " + max);
        if (pos == max) {
            return;
        } 
        // else, get the succeeding position and swap 
        Database.openConnection();
        
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("Select * FROM CoursesHaveModules "
                    + " WHERE course_name = ? and sequence_no = ?");
        pst.setString(1, courseName);
        int succeeding = pos+1;
        pst.setInt(2, succeeding);
        
        ResultSet rs = pst.executeQuery();
        System.out.println("Debug");
        System.out.println(rs.getInt(1)); // nothing here, perhaps cant do multiple ands?
        int id = rs.getInt(1);
        System.out.println("temp id storage:" + id);
        
        // set the current position to its preceding position
        PreparedStatement pst2 = Database.getSharedConnection().prepareStatement("Update CoursesHaveModules "
                    + " set sequence_no = ? where sequence_no = ? and course_name = ?");
        pst2.setInt(1, pos+1);
        pst2.setInt(2, pos);
        pst2.setString(3, courseName);
        pst2.executeUpdate();
        pst2.close();
        
        // set the preceding position to the currently selected position
        PreparedStatement pst3 = Database.getSharedConnection().prepareStatement("Update CoursesHaveModules "
                    + " set sequence_no = ? where chm_id = ?");
        pst3.setInt(1, pos);
        pst3.setInt(2, id);
        System.out.println("pos" + pos);
        pst3.executeUpdate();
        pst3.close();
        
        Database.closeConnection();
    }
    
    // This function deletes the respective record from chm table 
    public static void unlinkCourseModule(String selectedModule, String selectedCourse) throws SQLException {
        Database.openConnection();
        
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("DELETE FROM "
                + "CoursesHaveModules WHERE course_name = ? and module_name = ?");
        pst.setString(1, selectedCourse);
        pst.setString(2, selectedModule);
        pst.executeUpdate();
        
        pst.close();
        Database.closeConnection();
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
    
    public static ObservableList<Section> getSectionsWithinModule(Module selectedModule) throws SQLException {

        Database.openConnection();
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("Select * "
                + "From section where is_archived = 0 and module_name = ? order by sequence_no");
        
        pst.setString(1, selectedModule.getName());

        ResultSet rs = pst.executeQuery();

        //create new list to write over original observable list
        ObservableList<Section> newList = FXCollections.observableArrayList();

        while (rs.next()) {
            newList.add(new Section(new Module(rs.getString(2),"",0),  rs.getString(3), rs.getString(4), rs.getInt(5), rs.getInt(6)));
        }
        
        pst.close();
        Database.closeConnection();
        
        return newList;
    }
    
    public static ObservableList<Outcome> getOutcomesWithinModule(Module selectedModule) throws SQLException {

        Database.openConnection();
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("Select * "
                + "From ModuleLearningOutcomes where is_archived = 0 and module_name = ? order by sequence_no");
        
        pst.setString(1, selectedModule.getName());

        ResultSet rs = pst.executeQuery();

        //create new list to write over original observable list
        ObservableList<Outcome> newList = FXCollections.observableArrayList();

        while (rs.next()) {
            newList.add(new Outcome(new Module(rs.getString(2),"",0),  rs.getString(3), rs.getString(4), rs.getInt(5), rs.getInt(6)));
        }
        
        pst.close();
        Database.closeConnection();
        
        return newList;
    }
    
    /**
    * Helper functions for Section
    */

    public static void archiveSection(Section selectedSection) throws SQLException {
        Database.openConnection();
        
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("UPDATE section "
                + "set is_archived = 1 where section_name = ? and module_name = ?");
        pst.setString(1, selectedSection.getName());
        pst.setString(2, selectedSection.getsModule().getName());
        
        pst.executeUpdate();
        pst.close();
        Database.closeConnection();
    }
    
    public static void insertIntoSection(Section selectedSection) throws SQLException {
        Database.openConnection();

        //insert statement for new course
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("INSERT INTO "
                + "section (module_name, section_name, section_description, "
                + "sequence_no, is_archived) "
                + "VALUES (?,?,?,?,?)");
        pst.setString(1, selectedSection.getsModule().getName());
        pst.setString(2, selectedSection.getName());
        pst.setString(3, selectedSection.getDesc());
        pst.setInt(4, nextSectionNumber(selectedSection.getsModule().getName()));
        pst.setInt(5, selectedSection.getIsArchived());

        pst.executeUpdate();
        pst.close();
        Database.closeConnection();
    }
    
    public static int nextSectionNumber(String moduleName) throws SQLException {
        int highest = 0;
        Database.openConnection();
        
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("SELECT * FROM "
                + "section WHERE module_name = ? ");
        pst.setString(1, moduleName);
        
        ResultSet rs = pst.executeQuery();
        
        while (rs.next()) {
            if (rs.getInt(5) > highest) {
                highest = rs.getInt(5);
            }
        }
        highest++;
        
        pst.close();
        Database.closeConnection();
        return highest;
    }
    
    public static ObservableList<Section> getSections() throws SQLException {

        Database.openConnection();
        Statement st = Database.getSharedConnection().createStatement();

        String query = "Select * from section where is_archived = '0'";

        ResultSet rs = st.executeQuery(query);

        //create new list to write over original observable list
        ObservableList<Section> newList = FXCollections.observableArrayList();

        while (rs.next()) {
            newList.add(new Section(new Module(rs.getString(2),"",0),  rs.getString(3), rs.getString(4), rs.getInt(5), rs.getInt(6)));
        }
        
        st.close();
        Database.closeConnection();
        
        return newList;
    }
    
    // This function gets number of sections within the given module name
    public static int getSectionCount(String moduleName) throws SQLException {
        int sectionCount = 0;
        
        Database.openConnection();
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("SELECT * FROM "
                + "section WHERE module_name = ? and is_archived = 0");
        pst.setString(1, moduleName);
        ResultSet rs = pst.executeQuery();
        
        while (rs.next()) {
            sectionCount++;
        }
        
        pst.close();
        Database.closeConnection();
        return sectionCount;
    }
    
    public static ObservableList<Resource> getResourcesWithinSection(Section selectedSection) throws SQLException {

        Database.openConnection();
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("Select * "
                + "From Resource where is_archived = 0 and section_name = ?");
        
        pst.setString(1, selectedSection.getName());

        ResultSet rs = pst.executeQuery();

        //create new list to write over original observable list
        ObservableList<Resource> newList = FXCollections.observableArrayList();

        while (rs.next()) {
            Module dummyModule = new Module("","",0);
            Section dummySection = new Section(dummyModule, selectedSection.getName(), "", 1,0);
        
            //NOTE: I've set the extension to null for now. Decide later if
            // we want to implement the extension here or under attachments. 
            Resource newResource = new Resource(dummySection, rs.getString(3), "");
            newList.add(newResource);
        }
        
        pst.close();
        Database.closeConnection();
        
        return newList;
    }
    
    // This function parses the sections within a module name, and resets 
    // non-archived sections in ascending order, starting from 1.
    public static void sortSections(String moduleName) throws SQLException {
        System.out.println("Sorting the sections!");
        int sectionCount = getSectionCount(moduleName);
        System.out.println("Section count:" + sectionCount);
        Database.openConnection();
        
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("SELECT * FROM "
                + "section WHERE module_name = ?  and is_archived = 0 order by sequence_no");
        pst.setString(1, moduleName);
        
        ResultSet rs = pst.executeQuery();
        
        // initialize the minimum sequence number to the sequence_no attribute
        // in first response in the ResultSet
        
        int numSorted = 1;
        
        while (rs.next()) {
            while (numSorted <= sectionCount) {
                
                PreparedStatement pst2 = Database.getSharedConnection().prepareStatement("Update section "
                    + "set sequence_no = ? WHERE is_archived = 0 and section_id = ?");
                pst2.setInt(1, numSorted);
                pst2.setInt(2, rs.getInt(1));
                pst2.executeUpdate();
                pst2.close();

                System.out.println("sequence number to change to: " + numSorted);
                System.out.println("section id targeted: " + rs.getInt(1));
                numSorted++;
                rs.next();
            }
        }
        
        pst.close();
        Database.closeConnection();
    }
    
    // this function returns the int position of a given section and module
    public static int getSectionIntPos(String sectionName, String moduleName) throws SQLException {
        
        Database.openConnection();
        
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("Select * FROM section "
                    + " WHERE is_archived = 0 and section_name = ? and module_name = ?");
        pst.setString(1, sectionName);
        pst.setString(2, moduleName);
        ResultSet rs = pst.executeQuery();
        
        int pos = rs.getInt(5);
        
        pst.close();
        Database.closeConnection();
        
        return pos;
    }
    
    // this function returns number of sections in a given module
    public static int getNumSections(String moduleName) throws SQLException {
        
        Database.openConnection();
        
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("Select * FROM section "
                    + " WHERE is_archived = 0  and module_name = ?");
        pst.setString(1, moduleName);
        ResultSet rs = pst.executeQuery();
        
        int numSections = 0;
        
        while (rs.next()) {
            numSections++;
        }
        
        pst.close();
        Database.closeConnection();
        
        return numSections;
    }

    public static void shiftSectionUp(String selectedSection, String moduleName) throws SQLException {
        // get the integer position of the selected section
        int pos = getSectionIntPos(selectedSection, moduleName);
        // if position is 1, do nothing
        
        if (pos == 1) {
            return;
        } 
        // else, get the preceding position and swap 
        Database.openConnection();
        
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("Select * FROM section "
                    + " WHERE is_archived = 0 and module_name = ? and sequence_no = ?");
        pst.setString(1, moduleName);
        int preceding = pos-1;
        pst.setInt(2, preceding);
        
        ResultSet rs = pst.executeQuery();
        System.out.println("Debug");
        System.out.println(rs.getInt(1)); // nothing here, perhaps cant do multiple ands?
        int id = rs.getInt(1);
        System.out.println("temp id storage:" + id);
        
        // set the current position to its preceding position
        PreparedStatement pst2 = Database.getSharedConnection().prepareStatement("Update section "
                    + " set sequence_no = ? where sequence_no = ? and module_name = ?");
        pst2.setInt(1, pos-1);
        pst2.setInt(2, pos);
        pst2.setString(3, moduleName);
        pst2.executeUpdate();
        pst2.close();
        
        // set the preceding position to the currently selected position
        PreparedStatement pst3 = Database.getSharedConnection().prepareStatement("Update section "
                    + " set sequence_no = ? where section_id = ?");
        pst3.setInt(1, pos);
        pst3.setInt(2, id);
        System.out.println("pos" + pos);
        pst3.executeUpdate();
        pst3.close();
        
        Database.closeConnection();
    }
    
    public static void shiftSectionDown(String selectedSection, String moduleName) throws SQLException {
        // get the integer position of the selected section
        int pos = getSectionIntPos(selectedSection, moduleName);
        int max = getNumSections( moduleName); 
        // if position is max, do nothing
        System.out.println("pos: " + pos);
        System.out.println("Max: " + max);
        if (pos == max) {
            return;
        } 
        // else, get the succeeding position and swap 
        Database.openConnection();
        
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("Select * FROM section "
                    + " WHERE is_archived = 0 and module_name = ? and sequence_no = ?");
        pst.setString(1, moduleName);
        int succeeding = pos+1;
        pst.setInt(2, succeeding);
        
        ResultSet rs = pst.executeQuery();
        System.out.println("Debug");
        System.out.println(rs.getInt(1)); // nothing here, perhaps cant do multiple ands?
        int id = rs.getInt(1);
        System.out.println("temp id storage:" + id);
        
        // set the current position to its preceding position
        PreparedStatement pst2 = Database.getSharedConnection().prepareStatement("Update section "
                    + " set sequence_no = ? where sequence_no = ? and module_name = ?");
        pst2.setInt(1, pos+1);
        pst2.setInt(2, pos);
        pst2.setString(3, moduleName);
        pst2.executeUpdate();
        pst2.close();
        
        // set the preceding position to the currently selected position
        PreparedStatement pst3 = Database.getSharedConnection().prepareStatement("Update section "
                    + " set sequence_no = ? where section_id = ?");
        pst3.setInt(1, pos);
        pst3.setInt(2, id);
        System.out.println("pos" + pos);
        pst3.executeUpdate();
        pst3.close();
        
        Database.closeConnection();
    }
    
    /**
    * Helper functions for Resource
    */
    
    public static void archiveResource(Resource selectedResource) throws SQLException {
        Database.openConnection();
        
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("UPDATE Resource "
                + "set is_archived = 1 where resource_name = ? and section_name = ?");
        pst.setString(1, selectedResource.getName());
        pst.setString(2, selectedResource.getrSection().getName());
        
        pst.executeUpdate();
        pst.close();
        Database.closeConnection();
    }
    
    public static int nextResourceNumber(String sectionName) throws SQLException {
        int highest = 0;
        Database.openConnection();
        
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("SELECT * FROM "
                + "Resource WHERE section_name = ? ");
        pst.setString(1, sectionName);
        
        ResultSet rs = pst.executeQuery();
        
        while (rs.next()) {
            if (rs.getInt(5) > highest) {
                highest = rs.getInt(5);
            }
        }
        highest++;
        
        pst.close();
        Database.closeConnection();
        return highest;
    }
    
    public static void insertIntoResource(Resource selectedResource) throws SQLException {
        Database.openConnection();

        //insert statement for new course
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("INSERT INTO "
                + "Resource (section_name, resource_name, resource_ext, "
                + "is_archived) "
                + "VALUES (?,?,?,?)");
        pst.setString(1, selectedResource.getrSection().getName());
        pst.setString(2, selectedResource.getName());
        pst.setString(3, selectedResource.getExt());
        pst.setInt(4, 0);

        pst.executeUpdate();
        pst.close();
        Database.closeConnection();
    }
    
    public static ObservableList<Resource> getResources() throws SQLException {

        Database.openConnection();
        Statement st = Database.getSharedConnection().createStatement();

        String query = "Select * from Resource where is_archived = '0'";

        ResultSet rs = st.executeQuery(query);

        //create new list to write over original observable list
        ObservableList<Resource> newList = FXCollections.observableArrayList();

        while (rs.next()) {
            
            Module dummyModule = new Module("","",0);
            Section dummySection = new Section(dummyModule, rs.getString(2), "", 1, 0);
            Resource newResource = new Resource(dummySection, rs.getString(3), rs.getString(4));
            newList.add(newResource);
        }
        
        st.close();
        Database.closeConnection();
        
        return newList;
    }

    
    /**
    * Helper functions for Outcome
    */
    
    public static void archiveOutcome(Outcome selectedOutcome) throws SQLException {
        Database.openConnection();
        
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("UPDATE ModuleLearningOutcomes "
                + "set is_archived = 1 where mlo_name = ? and module_name = ?");
        pst.setString(1, selectedOutcome.getName());
        pst.setString(2, selectedOutcome.getoModule().getName());
        
        pst.executeUpdate();
        pst.close();
        Database.closeConnection();
    }
    
    public static int nextOutcomeNumber(String moduleName) throws SQLException {
        int highest = 0;
        Database.openConnection();
        
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("SELECT * FROM "
                + "ModuleLearningOutcomes WHERE module_name = ? ");
        pst.setString(1, moduleName);
        
        ResultSet rs = pst.executeQuery();
        
        while (rs.next()) {
            if (rs.getInt(5) > highest) {
                highest = rs.getInt(5);
            }
        }
        highest++;
        
        pst.close();
        Database.closeConnection();
        return highest;
    }
    
    public static void insertIntoOutcome(Outcome selectedOutcome) throws SQLException {
        Database.openConnection();

        //insert statement for new course
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("INSERT INTO "
                + "ModuleLearningOutcomes (module_name, mlo_name, mlo_description, "
                + "sequence_no, is_archived) "
                + "VALUES (?,?,?,?,?)");
        pst.setString(1, selectedOutcome.getoModule().getName());
        pst.setString(2, selectedOutcome.getName());
        pst.setString(3, selectedOutcome.getDesc());
        pst.setInt(4, nextOutcomeNumber(selectedOutcome.getoModule().getName()));
        pst.setInt(5, selectedOutcome.getIsArchived());

        pst.executeUpdate();
        pst.close();
        Database.closeConnection();
    }
    
    public static ObservableList<Outcome> getOutcomes() throws SQLException {

        Database.openConnection();
        Statement st = Database.getSharedConnection().createStatement();

        String query = "Select * from ModuleLearningOutcomes where is_archived = '0'";

        ResultSet rs = st.executeQuery(query);

        //create new list to write over original observable list
        ObservableList<Outcome> newList = FXCollections.observableArrayList();

        while (rs.next()) {
            newList.add(new Outcome(new Module(rs.getString(2),"",0),  rs.getString(3), rs.getString(4), rs.getInt(5), rs.getInt(6)));
        }
        
        st.close();
        Database.closeConnection();
        
        return newList;
    }
    
    // This function parses the sections within a module name, and resets 
    // non-archived sections in ascending order, starting from 1.
    public static void sortOutcomes(String moduleName) throws SQLException {
        System.out.println("Sorting the outcomes!");
        int outcomeCount = getNumOutcomes(moduleName);
        System.out.println("Outcome count:" + outcomeCount);
        Database.openConnection();
        
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("SELECT * FROM "
                + "ModuleLearningOutcomes WHERE module_name = ?  and is_archived = 0 order by sequence_no");
        pst.setString(1, moduleName);
        
        ResultSet rs = pst.executeQuery();
        
        // initialize the minimum sequence number to the sequence_no attribute
        // in first response in the ResultSet
        
        int numSorted = 1;
        
        while (rs.next()) {
            while (numSorted <= outcomeCount) {
                
                PreparedStatement pst2 = Database.getSharedConnection().prepareStatement("Update ModuleLearningOutcomes "
                    + "set sequence_no = ? WHERE is_archived = 0 and mlo_id = ?");
                pst2.setInt(1, numSorted);
                pst2.setInt(2, rs.getInt(1));
                pst2.executeUpdate();
                pst2.close();

                System.out.println("sequence number to change to: " + numSorted);
                System.out.println("mlo id targeted: " + rs.getInt(1));
                numSorted++;
                rs.next();
            }
        }
        
        pst.close();
        Database.closeConnection();
    }
    
    // this function returns the int position of a given section and module
    public static int getOutcomeIntPos(String outcomeName, String moduleName) throws SQLException {
        
        Database.openConnection();
        
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("Select * FROM ModuleLearningOutcomes "
                    + " WHERE is_archived = 0 and mlo_name = ? and module_name = ?");
        pst.setString(1, outcomeName);
        pst.setString(2, moduleName);
        ResultSet rs = pst.executeQuery();
        
        int pos = rs.getInt(5);
        
        pst.close();
        Database.closeConnection();
        
        return pos;
    }
    
    // this function returns number of sections in a given module
    public static int getNumOutcomes(String moduleName) throws SQLException {
        
        Database.openConnection();
        
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("Select * FROM ModuleLearningOutcomes "
                    + " WHERE is_archived = 0  and module_name = ?");
        pst.setString(1, moduleName);
        ResultSet rs = pst.executeQuery();
        
        int numOutcomes = 0;
        
        while (rs.next()) {
            numOutcomes++;
        }
        
        pst.close();
        Database.closeConnection();
        
        return numOutcomes;
    }

    public static void shiftOutcomeUp(String selectedOutcome, String moduleName) throws SQLException {
        // get the integer position of the selected section
        int pos = getOutcomeIntPos(selectedOutcome, moduleName);
        // if position is 1, do nothing
        
        if (pos == 1) {
            return;
        } 
        // else, get the preceding position and swap 
        Database.openConnection();
        
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("Select * FROM ModuleLearningOutcomes "
                    + " WHERE is_archived = 0 and module_name = ? and sequence_no = ?");
        pst.setString(1, moduleName);
        int preceding = pos-1;
        pst.setInt(2, preceding);
        
        ResultSet rs = pst.executeQuery();
        System.out.println("Debug");
        System.out.println(rs.getInt(1)); // nothing here, perhaps cant do multiple ands?
        int id = rs.getInt(1);
        System.out.println("temp id storage:" + id);
        
        // set the current position to its preceding position
        PreparedStatement pst2 = Database.getSharedConnection().prepareStatement("Update ModuleLearningOutcomes "
                    + " set sequence_no = ? where sequence_no = ? and module_name = ?");
        pst2.setInt(1, pos-1);
        pst2.setInt(2, pos);
        pst2.setString(3, moduleName);
        pst2.executeUpdate();
        pst2.close();
        
        // set the preceding position to the currently selected position
        PreparedStatement pst3 = Database.getSharedConnection().prepareStatement("Update ModuleLearningOutcomes "
                    + " set sequence_no = ? where mlo_id = ?");
        pst3.setInt(1, pos);
        pst3.setInt(2, id);
        System.out.println("pos" + pos);
        pst3.executeUpdate();
        pst3.close();
        
        Database.closeConnection();
    }
    
    public static void shiftOutcomeDown(String selectedOutcome, String moduleName) throws SQLException {
        // get the integer position of the selected section
        int pos = getOutcomeIntPos(selectedOutcome, moduleName);
        int max = getNumOutcomes( moduleName); 
        // if position is max, do nothing
        System.out.println("pos: " + pos);
        System.out.println("Max: " + max);
        if (pos == max) {
            return;
        } 
        // else, get the succeeding position and swap 
        Database.openConnection();
        
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("Select * FROM ModuleLearningOutcomes "
                    + " WHERE is_archived = 0 and module_name = ? and sequence_no = ?");
        pst.setString(1, moduleName);
        int succeeding = pos+1;
        pst.setInt(2, succeeding);
        
        ResultSet rs = pst.executeQuery();
        System.out.println("Debug");
        System.out.println(rs.getInt(1)); // nothing here, perhaps cant do multiple ands?
        int id = rs.getInt(1);
        System.out.println("temp id storage:" + id);
        
        // set the current position to its preceding position
        PreparedStatement pst2 = Database.getSharedConnection().prepareStatement("Update ModuleLearningOutcomes "
                    + " set sequence_no = ? where sequence_no = ? and module_name = ?");
        pst2.setInt(1, pos+1);
        pst2.setInt(2, pos);
        pst2.setString(3, moduleName);
        pst2.executeUpdate();
        pst2.close();
        
        // set the preceding position to the currently selected position
        PreparedStatement pst3 = Database.getSharedConnection().prepareStatement("Update ModuleLearningOutcomes "
                    + " set sequence_no = ? where mlo_id = ?");
        pst3.setInt(1, pos);
        pst3.setInt(2, id);
        System.out.println("pos" + pos);
        pst3.executeUpdate();
        pst3.close();
        
        Database.closeConnection();
    }
    
    /*
    * Helper methods for CoursesHaveModules
    */
    
    public static void insertIntoCoursesHaveModules(String courseName, String moduleName) throws SQLException {
        
        // get the next sequence number needed to insert first
        int seq = getModuleCount(courseName);
        
        Database.openConnection();
        
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("Insert into CoursesHaveModules "
                    + " (course_name, module_name, sequence_no) values (?,?,?)");
        pst.setString(1, courseName);
        pst.setString(2, moduleName);
        pst.setInt(3, seq);
        
        pst.executeUpdate();
        pst.close();
        Database.closeConnection();
    }

}
