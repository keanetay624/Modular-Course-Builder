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
                + "course_code TEXT NOT NULL, "
                + "sequence_no INTEGER NOT NULL, "
                + "FOREIGN KEY(module_name) REFERENCES Module(module_name),"
                + "FOREIGN KEY(course_code) REFERENCES Course(course_code)"
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
        
        //create Users
        String createStatement7 = "CREATE TABLE Users "
                + "("
                + "ID INTEGER PRIMARY KEY autoincrement, "
                + "Username TEXT NOT NULL, "
                + "Password TEXT NOT NULL "
                + ");";
        
        //create Attachment
        String createStatement8 = "CREATE TABLE Attachment ("
                + "Attachment_ID INTEGER PRIMARY KEY autoincrement, "
                + "Attachment_name TEXT NOT NULL, "
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
        SampleDataHelper.insertSampleData();
        
        //close connections
        st.close();
        sharedConnection.close();

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
        
        if (rs.next()) {
            validate = true;
        } 
        
        st.close();
        Database.closeConnection();
        
        return validate;
    }
}
