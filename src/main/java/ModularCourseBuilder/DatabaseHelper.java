/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModularCourseBuilder;

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
        
        pst.close();
        Database.closeConnection();
    }
    
    public static void updateCourse(Course selectedCourse, Course updatedCourse) throws SQLException {
        Database.openConnection();

        // Updated selectedCourse to updatedcourse
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("UPDATE Course "
                + "SET course_code = ?, course_name = ?, faculty_name = ?, school_name = ?, "
                + "level = ?, campus = ? WHERE course_code = ?");
        pst.setString(1, updatedCourse.getCourseCode());
        pst.setString(2, updatedCourse.getName());
        pst.setString(3, updatedCourse.getFaculty());
        pst.setString(4, updatedCourse.getSchool());
        pst.setInt(5,updatedCourse.getLevel());
        pst.setString(6, updatedCourse.getCampus());
        pst.setString(7, selectedCourse.getCourseCode());
        pst.executeUpdate();
        
        // Updated chm table to updated Course
        PreparedStatement pst2 = Database.getSharedConnection().prepareStatement("UPDATE CoursesHaveModules "
                + "SET course_code = ? "
                + "WHERE course_code = ?");
        pst2.setString(1, updatedCourse.getCourseCode());
        pst2.setString(2, updatedCourse.getCourseCode());
        pst2.executeUpdate();
        
        // Updated Attachment table
        PreparedStatement pst3 = Database.getSharedConnection().prepareStatement("UPDATE Attachment "
                + "SET course_id = ? "
                + "WHERE course_id = ?");
        pst3.setString(1, updatedCourse.getCourseCode());
        pst3.setString(2, updatedCourse.getCourseCode());
        pst3.executeUpdate();

        pst.close();
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
                + "From CoursesHaveModules where course_code = ? order by sequence_no");
        
        pst.setString(1, selectedCourse.getCourseCode());

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
    
    // This function gets number of Modules within the given course code
    public static int getModuleCount(String courseCode) throws SQLException {
        int moduleCount = 0;
        
        Database.openConnection();
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("SELECT * FROM "
                + "CoursesHaveModules WHERE course_code = ?");
        pst.setString(1, courseCode);
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
    public static void sortModules(String courseCode) throws SQLException {
        int moduleCount = getModuleCount(courseCode);
        Database.openConnection();
        
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("SELECT * FROM "
                + "CoursesHaveModules WHERE course_code = ? order by sequence_no");
        pst.setString(1, courseCode);
        
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

                numSorted++;
                rs.next();
            }
        }
        
        pst.close();
        Database.closeConnection();
    }
    
    // this function returns the int position of a given module and course
    public static int getModuleIntPos(String moduleName, String courseCode) throws SQLException {
        
        Database.openConnection();
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("Select * FROM CoursesHaveModules "
                    + " WHERE module_name = ? and course_code = ?");
        pst.setString(1, moduleName);
        pst.setString(2, courseCode);
        ResultSet rs = pst.executeQuery();
        
        int pos = rs.getInt(4);
        
        pst.close();
        Database.closeConnection();
        
        return pos;
    }
    
    // this function returns number of Modules in a given Course
    public static int getNumModules(String courseCode) throws SQLException {
        
        Database.openConnection();
        
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("Select * FROM CoursesHaveModules "
                    + " WHERE course_code = ?");
        pst.setString(1, courseCode);
        ResultSet rs = pst.executeQuery();
        
        int numModules = 0;
        
        while (rs.next()) {
            numModules++;
        }
        
        pst.close();
        Database.closeConnection();
        
        return numModules;
    }
    
    public static void shiftModuleUp(String selectedModule, String courseCode) throws SQLException {
        // get the integer position of the selected Module
        int pos = getModuleIntPos(selectedModule, courseCode);
        // if position is 1, do nothing
        
        if (pos == 1) {
            return;
        } 
        // else, get the preceding position and swap 
        Database.openConnection();
        
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("Select * FROM CoursesHaveModules "
                    + " WHERE course_code = ? and sequence_no = ?");
        pst.setString(1, courseCode);
        int preceding = pos-1;
        pst.setInt(2, preceding);
        
        ResultSet rs = pst.executeQuery();
        int id = rs.getInt(1);
        
        // set the current position to its preceding position
        PreparedStatement pst2 = Database.getSharedConnection().prepareStatement("Update CoursesHaveModules "
                    + " set sequence_no = ? where sequence_no = ? and courseCode = ?");
        pst2.setInt(1, pos-1);
        pst2.setInt(2, pos);
        pst2.setString(3, courseCode);
        pst2.executeUpdate();
        pst2.close();
        
        // set the preceding position to the currently selected position
        PreparedStatement pst3 = Database.getSharedConnection().prepareStatement("Update CoursesHaveModules "
                    + " set sequence_no = ? where chm_id = ?");
        pst3.setInt(1, pos);
        pst3.setInt(2, id);
        pst3.executeUpdate();
        pst3.close();
        
        Database.closeConnection();
    }
    
    public static void shiftModuleDown(String selectedModule, String courseCode) throws SQLException {
        // get the integer position of the selected Module
        int pos = getModuleIntPos(selectedModule, courseCode); 
        int max = getNumModules(courseCode); 
        // if position is max, do nothing
        if (pos == max) {
            return;
        } 
        // else, get the succeeding position and swap 
        Database.openConnection();
        
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("Select * FROM CoursesHaveModules "
                    + " WHERE course_code = ? and sequence_no = ?");
        pst.setString(1, courseCode);
        int succeeding = pos+1;
        pst.setInt(2, succeeding);
        
        ResultSet rs = pst.executeQuery();
        int id = rs.getInt(1);
        
        // set the current position to its preceding position
        PreparedStatement pst2 = Database.getSharedConnection().prepareStatement("Update CoursesHaveModules "
                    + " set sequence_no = ? where sequence_no = ? and course_code = ?");
        pst2.setInt(1, pos+1);
        pst2.setInt(2, pos);
        pst2.setString(3, courseCode);
        pst2.executeUpdate();
        pst2.close();
        
        // set the preceding position to the currently selected position
        PreparedStatement pst3 = Database.getSharedConnection().prepareStatement("Update CoursesHaveModules "
                    + " set sequence_no = ? where chm_id = ?");
        pst3.setInt(1, pos);
        pst3.setInt(2, id);
        pst3.executeUpdate();
        pst3.close();
        
        Database.closeConnection();
    }
    
    // This function deletes the respective record from chm table 
    public static void unlinkCourseModule(String selectedModule, String selectedCourse) throws SQLException {
        Database.openConnection();
        
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("DELETE FROM "
                + "CoursesHaveModules WHERE course_code = ? and module_name = ?");
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
    
    public static void updateModule(Module selectedModule, Module updatedModule) throws SQLException {
        Database.openConnection();
        Statement st = Database.getSharedConnection().createStatement();

        // Updated selectedModule to updated Module
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("UPDATE Module "
                + "SET module_name = ?, module_description = ? "
                + "WHERE module_name = ?");
        pst.setString(1, updatedModule.getName());
        pst.setString(2, updatedModule.getDesc());
        pst.setString(3, selectedModule.getName());
        pst.executeUpdate();
        
        // Updated chm table to updated Module
        PreparedStatement pst2 = Database.getSharedConnection().prepareStatement("UPDATE CoursesHaveModules "
                + "SET module_name = ? "
                + "WHERE module_name = ?");
        pst2.setString(1, updatedModule.getName());
        pst2.setString(2, selectedModule.getName());
        pst2.executeUpdate();
        
        // Updated outcomes table to updated Module
        PreparedStatement pst3 = Database.getSharedConnection().prepareStatement("UPDATE ModuleLearningOutcomes "
                + "SET module_name = ?"
                + "WHERE module_name = ?");
        pst3.setString(1, updatedModule.getName());
        pst3.setString(2, selectedModule.getName());
        pst3.executeUpdate();
        
        // Updated sections table to updated Module
        PreparedStatement pst4 = Database.getSharedConnection().prepareStatement("UPDATE Section "
                + "SET module_name = ?"
                + "WHERE module_name = ?");
        pst4.setString(1, updatedModule.getName());
        pst4.setString(2, selectedModule.getName());
        pst4.executeUpdate();
        
        // Updated Attachment table
        PreparedStatement pst5 = Database.getSharedConnection().prepareStatement("UPDATE Attachment "
                + "SET module_id = ? "
                + "WHERE module_id = ?");
        pst5.setString(1, updatedModule.getName());
        pst5.setString(2, selectedModule.getName());
        pst5.executeUpdate();
        
        st.close();
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
        int sectionCount = getSectionCount(moduleName);
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
        int id = rs.getInt(1);
        
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
        pst3.executeUpdate();
        pst3.close();
        
        Database.closeConnection();
    }
    
    public static void shiftSectionDown(String selectedSection, String moduleName) throws SQLException {
        // get the integer position of the selected section
        int pos = getSectionIntPos(selectedSection, moduleName);
        int max = getNumSections( moduleName); 
        // if position is max, do nothing
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
        int id = rs.getInt(1);
        
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
        pst3.executeUpdate();
        pst3.close();
        
        Database.closeConnection();
    }
    
    public static void updateSection(Section selectedSection, Section updatedSection) throws SQLException {
        Database.openConnection();
        Statement st = Database.getSharedConnection().createStatement();

        // Updated selectedCourse to updatedcourse
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("UPDATE Section "
                + "SET module_name = ?, section_name = ?, section_description = ?"
                + "WHERE (section_name = ? and module_name = ?)");
        pst.setString(1, updatedSection.getsModule().getName());
        pst.setString(2, updatedSection.getName());
        pst.setString(3, updatedSection.getDesc());
        pst.setString(4, selectedSection.getName());
        pst.setString(5, selectedSection.getsModule().getName());
        pst.executeUpdate();
        
        st.close();
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
    
    public static void updateResource(Resource selectedResource, Resource updatedResource) throws SQLException {
        Database.openConnection();

        // Updated selectedResource to updatedResource
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("UPDATE Resource "
                + "SET section_name = ?, resource_name = ?, resource_ext = ?"
                + "WHERE (section_name = ? and resource_name = ?)");
        pst.setString(1, updatedResource.getrSection().getName());
        pst.setString(2, updatedResource.getName());
        pst.setString(3, updatedResource.getExt());
        pst.setString(4, selectedResource.getrSection().getName());
        pst.setString(5, selectedResource.getName());
        pst.executeUpdate();
        
        pst.close();
        Database.closeConnection();
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
    
    // returns the selected outcome to the user
    public static Outcome getSelectedOutcome(Module selectedModule, String outcomeString) throws SQLException {
        Database.openConnection();
        
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("SELECT * From  "
                + "ModuleLearningOutcomes WHERE module_name = ? and mlo_name = ?");
        pst.setString(1, selectedModule.getName());
        pst.setString(2, outcomeString);
        
        ResultSet rs = pst.executeQuery();
        Module dummy = new Module(rs.getString(2),"",0);
        Outcome selectedOutcome = new Outcome(dummy, rs.getString(3), rs.getString(4), rs.getInt(5), rs.getInt(6));
                
        pst.close();
        Database.closeConnection();
        
        return selectedOutcome;
    }
    
    public static void updateOutcome(Outcome selectedOutcome, Outcome updatedOutcome) throws SQLException {
        Database.openConnection();
        Statement st = Database.getSharedConnection().createStatement();

        // Updated selectedCourse to updatedcourse
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("UPDATE ModuleLearningOutcomes "
                + "SET module_name = ?, mlo_name = ?, mlo_description = ?"
                + "WHERE (module_name = ? and mlo_name = ?)");
        pst.setString(1, updatedOutcome.getoModule().getName());
        pst.setString(2, updatedOutcome.getName());
        pst.setString(3, updatedOutcome.getDesc());
        pst.setString(4, selectedOutcome.getoModule().getName());
        pst.setString(5, selectedOutcome.getName());
        pst.executeUpdate();
        
        st.close();
        Database.closeConnection();
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
        int outcomeCount = getNumOutcomes(moduleName);
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
        int id = rs.getInt(1);
        
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
        pst3.executeUpdate();
        pst3.close();
        
        Database.closeConnection();
    }
    
    public static void shiftOutcomeDown(String selectedOutcome, String moduleName) throws SQLException {
        // get the integer position of the selected section
        int pos = getOutcomeIntPos(selectedOutcome, moduleName);
        int max = getNumOutcomes( moduleName); 
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
        int id = rs.getInt(1);
        
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
        pst3.executeUpdate();
        pst3.close();
        
        Database.closeConnection();
    }
    
    /*
    * Helper methods for CoursesHaveModules
    */
    
    public static void insertIntoCoursesHaveModules(String courseCode, String moduleName) throws SQLException {
        
        // get the next sequence number needed to insert first
        int seq = getModuleCount(courseCode);
        
        Database.openConnection();
        
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("Insert into CoursesHaveModules "
                    + " (course_code, module_name, sequence_no) values (?,?,?)");
        pst.setString(1, courseCode);
        pst.setString(2, moduleName);
        pst.setInt(3, seq);
        
        pst.executeUpdate();
        pst.close();
        Database.closeConnection();
    }
    
    /*
    * Helper methods for Attachments 
    */
    
    public static String getFileName(int modelType, String idString, String idString2) throws SQLException {
       Database.openConnection();
       
       String name = "";
       
       if (modelType == 1) {
           PreparedStatement pst = 
                   Database.getSharedConnection().prepareStatement("Select * From "
                           + "Attachment Where course_id = ?");
           pst.setString(1, idString);
           ResultSet rs = pst.executeQuery();
           
           if (rs.next()) {
               name = rs.getString(2);
           }
           pst.close();
       } else if (modelType == 2) {
           PreparedStatement pst = 
                   Database.getSharedConnection().prepareStatement("Select * From "
                           + "Attachment Where module_id = ?");
           pst.setString(1, idString);
           ResultSet rs = pst.executeQuery();
           
           if (rs.next()) {
               name = rs.getString(2);
           }
           pst.close();
       } else if (modelType == 3) {
           PreparedStatement pst0 = 
                   Database.getSharedConnection().prepareStatement("Select section_id From "
                           + "Section Where section_name = ? and module_name = ?");
           pst0.setString(1, idString);
           pst0.setString(2, idString2);
           
           ResultSet rs = pst0.executeQuery();
           
           int id = rs.getInt(1);
           
           PreparedStatement pst = 
                   Database.getSharedConnection().prepareStatement("Select * From "
                           + "Attachment Where section_id = ?");
           pst.setInt(1, id);
           ResultSet rs2 = pst.executeQuery();
           
           if (rs2.next()) {
               name = rs2.getString(2);
           }
           pst.close();
       } else {
           PreparedStatement pst0 = 
                   Database.getSharedConnection().prepareStatement("Select resource_id From "
                           + "Resource Where resource_name = ? and section_name = ?");
           pst0.setString(1, idString);
           pst0.setString(2, idString2);
           System.out.println("resource_name: " + idString);
           System.out.println("section_name: " + idString2);
           
           ResultSet rs = pst0.executeQuery();
           
           int id = rs.getInt(1);
           
            PreparedStatement pst = 
                   Database.getSharedConnection().prepareStatement("Select * From "
                           + "Attachment Where resource_id = ?");
            pst.setInt(1, id);
            ResultSet rs2 = pst.executeQuery();
           
           if (rs2.next()) {
               name = rs2.getString(2);
           }
           pst.close();
       }
       
       Database.closeConnection();
       
       return name;
    }
    
    public static void insertCourseAttachment(byte[] convertedFile, String name, String course_code) throws SQLException {
       Database.openConnection();

       PreparedStatement pst = 
                   Database.getSharedConnection().prepareStatement("INSERT INTO "
                           + "Attachment (attachment_name, "
                           + "course_id, blob_data)"
                           + "VALUES (?,?,?)");
       pst.setString(1, name);
       pst.setString(2, course_code);
       pst.setBytes(3, convertedFile);
       pst.executeUpdate();

       Database.closeConnection();
       pst.close();
    }
     
    public static void insertModuleAttachment(byte[] convertedFile, String name, String module_name) throws SQLException {
        Database.openConnection();

       PreparedStatement pst = 
                   Database.getSharedConnection().prepareStatement("INSERT INTO "
                           + "Attachment (attachment_name, "
                           + "module_id, blob_data)"
                           + "VALUES (?,?,?)");
       pst.setString(1, name);
       pst.setString(2, module_name);
       pst.setBytes(3, convertedFile);
       pst.executeUpdate();

       Database.closeConnection();
       pst.close();
    }
     
     public static void insertSectionAttachment(byte[] convertedFile, String name, int section_id) throws SQLException {
        Database.openConnection();
        
        PreparedStatement pst = 
                    Database.getSharedConnection().prepareStatement("INSERT INTO "
                            + "Attachment (attachment_name, "
                            + "section_id, blob_data)"
                            + "VALUES (?,?,?)");
        pst.setString(1, name);
        pst.setInt(2, section_id);
        pst.setBytes(3, convertedFile);
        pst.executeUpdate();

        Database.closeConnection();
        pst.close();
    }
    
    public static void insertResourceAttachment(byte[] convertedFile, String name, int resource_id) throws SQLException {
        Database.openConnection();
        
        PreparedStatement pst = 
                    Database.getSharedConnection().prepareStatement("INSERT INTO "
                            + "Attachment (attachment_name, "
                            + "resource_id, blob_data)"
                            + "VALUES (?,?,?)");
        pst.setString(1, name);
        pst.setInt(2, resource_id);
        pst.setBytes(3, convertedFile);
        pst.executeUpdate();

        Database.closeConnection();
        pst.close();
    }
    
    /**
    * Helper functions for User 
    */
    public static boolean userExists(String username) throws SQLException {
        boolean userExists = false;
        
        Database.openConnection();
        PreparedStatement pst = 
                    Database.getSharedConnection().prepareStatement("Select * From "
                            + "Users WHERE username = ?");
        pst.setString(1, username);
        ResultSet rs = pst.executeQuery();
        
        if (rs.next()) {
            userExists = true;
        } 
        Database.closeConnection();
        pst.close();
        
        return userExists;
    }
    
    public static void insertIntoUser(String username, String password) throws SQLException {
        Database.openConnection();
        PreparedStatement pst = 
                    Database.getSharedConnection().prepareStatement("Insert into users "
                            + "(username, password) values (?,?)");
        pst.setString(1, username);
        pst.setString(2, password);
        pst.executeUpdate();
        
        Database.closeConnection();
        pst.close();
    }
    
}
