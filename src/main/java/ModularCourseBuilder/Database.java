/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModularCourseBuilder;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Keane Local
 */
public class Database {
    //Database variables
    final private String database= "jdbc:sqlite:CourseDatabase.db";
    private static Connection sharedConnection;

    //getters and setters

    public static Connection getSharedConnection() {
        return sharedConnection;
    }

    public static void setSharedConnection(Connection sharedConnection) {
        Database.sharedConnection = sharedConnection;
    }
    
    //database methods

    public void setupDatabase() throws SQLException {

        Database.openConnection();
        Statement st = sharedConnection.createStatement();
        
        //create Module table
        String createStatement1 = "CREATE TABLE Module"
                + "(module_name TEXT PRIMARY KEY NOT NULL,"
                + "module_description TEXT NOT NULL,"
                + "is_archived INTEGER NOT NULL check "
                + "(is_archived between 0 and 1));";
        
        
        // create Section table
        String createStatement2 = "CREATE TABLE Section"
                + "(section_id INTEGER PRIMARY KEY autoincrement, "
                + "module_name TEXT NOT NULL, "
                + "section_name TEXT NOT NULL,"
                + "section_description TEXT NOT NULL,"
                + "sequence_no INTEGER NOT NULL,"
                + "is_archived INTEGER NOT NULL check "
                + "(is_archived between 0 and 1),"
                + "FOREIGN KEY(module_name) REFERENCES Module(module_name)"
                + ");";
        
        //create Resource Table
        // trying new table structure
        String createStatement3 = "CREATE TABLE Resource"
                + "(resource_id INTEGER PRIMARY KEY autoincrement, "
                + "section_name TEXT NOT NULL, "
                + "resource_name TEXT NOT NULL, "
                + "resource_ext TEXT NOT NULL, "
                + "is_archived INTEGER NOT NULL check "
                + "(is_archived between 0 and 1), "
                + "FOREIGN KEY(section_name) REFERENCES Section(section_name) "
                + ");";

        //create Course Table
        String createStatement4 = "CREATE TABLE Course"
                + "(course_code TEXT PRIMARY KEY NOT NULL,"
                + "course_name TEXT NOT NULL,"
                + "faculty_name TEXT NOT NULL,"
                + "school_name TEXT NOT NULL,"
                + "level INTEGER NOT NULL,"
                + "campus TEXT NOT NULL,"
                + "is_archived INTEGER NOT NULL check "
                + "(is_archived between 0 and 1));";

        //create CoursesHaveModules
        String createStatement5 = "CREATE TABLE CoursesHaveModules"
                + "(chm_id INTEGER PRIMARY KEY autoincrement,"
                + "module_name TEXT NOT NULL, "
                + "course_name TEXT NOT NULL, "
                + "sequence_no INTEGER NOT NULL, "
                + "FOREIGN KEY(module_name) REFERENCES Module(module_name),"
                + "FOREIGN KEY(course_name) REFERENCES Module(course_name)"
                + ");";
                

        //create ModuleLearningOutcomes
        String createStatement6 = "CREATE TABLE ModuleLearningOutcomes"
                + "(mlo_id INTEGER PRIMARY KEY autoincrement, "
                + "module_name TEXT NOT NULL, "
                + "mlo_name TEXT NOT NULL,"
                + "mlo_description TEXT NOT NULL,"
                + "sequence_no INTEGER NOT NULL,"
                + "is_archived INTEGER NOT NULL check "
                + "(is_archived between 0 and 1), "
                + "FOREIGN KEY(module_name) REFERENCES Module(module_name)"
                + ");";

        String createStatement7 = "CREATE TABLE Users "
                + "("
                + "ID INTEGER PRIMARY KEY autoincrement, "
                + "Username TEXT NOT NULL, "
                + "Password TEXT NOT NULL "
                + ");";
        
        String createStatement8 = "CREATE TABLE Attachment ("
                + "Attachment_ID INTEGER PRIMARY KEY autoincrement, "
                + "Attachment_name TEXT NOT NULL, "
//                + "Attachment_ext Text NOT NULL, "
                + "Course_ID TEXT, Module_ID TEXT, Section_ID INTEGER, "
                + "Resource_ID INTEGER, BLOB_Data BLOB,"
                + "FOREIGN KEY(Course_ID) REFERENCES Course(course_code),"
                + "FOREIGN KEY(Module_ID) REFERENCES Module(module_name),"
                + "FOREIGN KEY(Section_ID) REFERENCES Section(section_id),"
                + "FOREIGN KEY(Resource_ID) REFERENCES Resource(resource_id));";
        
        // create Prerequisite table
        String createStatement9 = "CREATE TABLE Prerequisite "
                + "(Prerequisite_ID INTEGER PRIMARY KEY autoincrement, "
                + "MasterCourse_ID INTEGER NOT NULL,"
                + "Candidate_Course_ID INTEGER NOT NULL,"
                + "Candidate2_Course_ID INTEGER);";
        
        // enable foreign key support
        st.execute("PRAGMA foreign_keys = ON;");
        
        st.execute(createStatement1);
        st.execute(createStatement2);
        st.execute(createStatement3);
        st.execute(createStatement4);
        st.execute(createStatement5);
        st.execute(createStatement6);
        st.execute(createStatement7);
        st.execute(createStatement8);
        st.execute(createStatement9);
        
        // Insert sample data    
        insertSampleData();
        
        //close connections
        st.close();
        sharedConnection.close();

    }
    
    private void insertSampleData() throws SQLException {
        Database.openConnection();
        Statement st = Database.getSharedConnection().createStatement();

        //insert statement for new course
        PreparedStatement pst = Database.getSharedConnection().prepareStatement("INSERT INTO "
                + "course (course_code, course_name, faculty_name, school_name, level, campus, is_archived) "
                + "VALUES (?,?,?,?,?,?,?)");
        pst.setString(1, "ACCT2101");
        pst.setString(2, "Industry Placement 1");
        pst.setString(3, "UNSW Business School");
        pst.setString(4, "School of Accounting, Auditing and Taxation");
        pst.setInt(5, 0);
        pst.setString(6, "Sydney");
        pst.setInt(7,0);
        
        //insert statement for new module
        PreparedStatement pst2 = Database.getSharedConnection().prepareStatement("INSERT INTO "
                + "module (module_name, module_description, is_archived) "
                + "VALUES (?,?,?)");
        pst2.setString(1, "Importance of Managing Information Systems");
        pst2.setString(2, "Goals of Information Security");
        pst2.setInt(3, 0);
        
        //insert statement for users
        PreparedStatement pst3 = Database.getSharedConnection().prepareStatement("INSERT INTO "
                + "users (username, password) "
                + "VALUES (?,?)");
        pst3.setString(1, "keane");
        pst3.setString(2, "iyashi");
        
        //insert statement for section
        PreparedStatement pst4 = Database.getSharedConnection().prepareStatement("INSERT INTO "
                + "section (module_name, section_name, "
                + "section_description, sequence_no, is_archived) "
                + "VALUES (?,?,?,?,?)");
        pst4.setString(1, "Importance of Managing Information Systems");
        pst4.setString(2, "What are Information Systems?");
        pst4.setString(3, "This section defines what Information Systems are");
        pst4.setInt(4, 1);
        pst4.setInt(5, 0);
        
        //insert statement for outcome
        PreparedStatement pst5 = Database.getSharedConnection().prepareStatement("INSERT INTO "
                + "ModuleLearningOutcomes (module_name, mlo_name, "
                + "mlo_description, sequence_no, is_archived) "
                + "VALUES (?,?,?,?,?)");
        pst5.setString(1, "Importance of Managing Information Systems");
        pst5.setString(2, "Understand how to manage information systems");
        pst5.setString(3, "Students must be able to effectively manage Information Systems");
        pst5.setInt(4, 1);
        pst5.setInt(5, 0);
        
        //insert statement for resource
        PreparedStatement pst6 = Database.getSharedConnection().prepareStatement("INSERT INTO "
                + "Resource (section_name, resource_name, "
                + "resource_ext, is_archived) "
                + "VALUES (?,?,?,?)");
        pst6.setString(1, "What are Information Systems?");
        pst6.setString(2, "Section Readme");
        pst6.setString(3, ".docx");
        pst6.setInt(4, 0);
        
        //insert statement for chm
        PreparedStatement pst7 = Database.getSharedConnection().prepareStatement("INSERT INTO "
                + "CoursesHaveModules (module_name, course_name, sequence_no) "
                + "VALUES (?,?,?)");
        pst7.setString(1, "Importance of Managing Information Systems");
        pst7.setString(2, "Industry Placement 1");
        pst7.setInt(3, 1);
        
        //insert statement for second module
        PreparedStatement pst8 = Database.getSharedConnection().prepareStatement("INSERT INTO "
                + "module (module_name, module_description, is_archived) "
                + "VALUES (?,?,?)");
        pst8.setString(1, "Second Module for Testing");
        pst8.setString(2, "Arbitrary Description");
        pst8.setInt(3, 0);
        
        pst.executeUpdate();
        pst2.executeUpdate();
        pst3.executeUpdate();
        pst4.executeUpdate();
        pst5.executeUpdate();
        pst6.executeUpdate();
        pst7.executeUpdate();
        pst8.executeUpdate();
        Database.closeConnection();
    }
    
    public static boolean CheckIfAlreadySetUp() throws SQLException {

        Database.openConnection();
        DatabaseMetaData dbmd = sharedConnection.getMetaData();
        ResultSet rs = dbmd.getTables(null, null, "Course", null);
        boolean databaseIsSetup = false;

        if (rs.next()) {
            databaseIsSetup = true;
        }

        Database.closeConnection();
        return databaseIsSetup;
    }
    // can use the conditional version in planet demo once we have added input and other functionality

    public static boolean openConnection() {
        boolean connectionStatus = false;
        try {
            Database.sharedConnection = DriverManager.getConnection("jdbc:sqlite:CourseDatabase.db");
            connectionStatus = true;
        } catch (SQLException e) { // equivalent of throws SQLException
            e.printStackTrace();
        } finally {
            return connectionStatus;
        }
    }

    public static boolean closeConnection() {
        boolean connectionStatus = false;
        try {
            sharedConnection.close();
            connectionStatus = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return connectionStatus;
        }
    }
    
    public static boolean validateUser(String username, String password) throws SQLException {
        boolean validate = false;
        
        Database.openConnection();
        
        Statement st = Database.getSharedConnection().createStatement();
        String query = "SELECT * FROM Users WHERE USERNAME = '" + username + "' AND PASSWORD = '" + password + "'";
        ResultSet rs = st.executeQuery(query);
        System.out.println("Validating...");
        
        if (rs.next()) {
            validate = true;
        } 
        
        st.close();
        Database.closeConnection();
        
        return validate;
    }
}
