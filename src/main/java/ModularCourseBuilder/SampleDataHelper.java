/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModularCourseBuilder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Keane Local
 */
public class SampleDataHelper {
    public static void insertSampleData() throws SQLException {
        Database.openConnection();
        Statement st = Database.getSharedConnection().createStatement();
        // Insert courses
        
        String[] courses = {
            "insert into Course (course_code, course_name, faculty_name, school_name, level, campus, is_archived) values ('ACCT2511', 'Financial Accounting Fundamentals', 'UNSW Business School', 'School of Accounting, Auditing and Taxation', 0, 'Sydney', 0);",
            "insert into Course (course_code, course_name, faculty_name, school_name, level, campus, is_archived) values ('ACCT2522', 'Management Accounting 1', 'UNSW Business School', 'School of Accounting, Auditing and Taxation', 0, 'Sydney', 0);",
            "insert into Course (course_code, course_name, faculty_name, school_name, level, campus, is_archived) values ('ACCT3583', 'Management Accounting 2', 'UNSW Business School', 'School of Accounting, Auditing and Taxation', 0, 'Sydney', 0);",
            "insert into Course (course_code, course_name, faculty_name, school_name, level, campus, is_archived) values ('ACTL3162', 'General Insurance Techniques', 'UNSW Business School', 'School of Risk and Actuarial Studies', 0, 'Sydney', 0);",  
            "insert into Course (course_code, course_name, faculty_name, school_name, level, campus, is_archived) values ('ACTL2101', 'Industry Placement 1', 'UNSW Business School', 'School of Risk and Actuarial Studies', 0, 'Sydney', 0);",  
            "insert into Course (course_code, course_name, faculty_name, school_name, level, campus, is_archived) values ('ACTL2102', 'Foundations of Actuarial Models', 'UNSW Business School', 'School of Risk and Actuarial Studies', 0, 'Sydney', 0);",
            "insert into Course (course_code, course_name, faculty_name, school_name, level, campus, is_archived) values ('ACTL2111', 'Financial Mathematics for Actuaries', 'UNSW Business School', 'School of Risk and Actuarial Studies', 0, 'Sydney', 0);",
            "insert into Course (course_code, course_name, faculty_name, school_name, level, campus, is_archived) values ('COMM1040', 'Entrepreneurial Ecosystems', 'UNSW Business School', 'General Education', 0, 'Sydney', 0);",
            "insert into Course (course_code, course_name, faculty_name, school_name, level, campus, is_archived) values ('COMM1100', 'Business Decision Making', 'UNSW Business School', 'General Education', 0, 'Sydney', 0);",   
            "insert into Course (course_code, course_name, faculty_name, school_name, level, campus, is_archived) values ('COMM1110', 'Evidence-Based Problem Solving', 'UNSW Business School', 'General Education', 0, 'Sydney', 0);",  
            "insert into Course (course_code, course_name, faculty_name, school_name, level, campus, is_archived) values ('COMM1120', 'Collaboration and Innovation in Business', 'UNSW Business School', 'General Education', 0, 'Sydney', 0);",
            "insert into Course (course_code, course_name, faculty_name, school_name, level, campus, is_archived) values ('COMM1140', 'Financial Management', 'UNSW Business School', 'General Education', 0, 'Sydney', 0);",
            "insert into Course (course_code, course_name, faculty_name, school_name, level, campus, is_archived) values ('INFS1602', 'Digital Transformation in Business', 'UNSW Business School', 'School of Information Systems and Technology Management', 0, 'Sydney', 0);",                                                      
            "insert into Course (course_code, course_name, faculty_name, school_name, level, campus, is_archived) values ('INFS1603', 'Introduction to Business Databases', 'UNSW Business School', 'School of Information Systems and Technology Management', 0, 'Sydney', 0);",
            "insert into Course (course_code, course_name, faculty_name, school_name, level, campus, is_archived) values ('INFS1609', 'Fundamentals of Business Programming', 'UNSW Business School', 'School of Information Systems and Technology Management', 0, 'Sydney', 0);", 
            "insert into Course (course_code, course_name, faculty_name, school_name, level, campus, is_archived) values ('INFS1701', 'Networking and Security', 'UNSW Business School', 'School of Information Systems and Technology Management', 0, 'Sydney', 0);", 
            "insert into Course (course_code, course_name, faculty_name, school_name, level, campus, is_archived) values ('INFS2101', 'Industry Placement 1', 'UNSW Business School', 'School of Information Systems and Technology Management', 0, 'Sydney', 0);", 
            "insert into Course (course_code, course_name, faculty_name, school_name, level, campus, is_archived) values ('INFS2602', 'Managing Information Systems', 'UNSW Business School', 'School of Information Systems and Technology Management', 0, 'Sydney', 0);", 
            "insert into Course (course_code, course_name, faculty_name, school_name, level, campus, is_archived) values ('INFS2603', 'Business Analysis and Agile Product Management', 'UNSW Business School', 'School of Information Systems and Technology Management', 0, 'Sydney', 0);", 
            "insert into Course (course_code, course_name, faculty_name, school_name, level, campus, is_archived) values ('INFS2605', 'Intermediate Business Programming', 'UNSW Business School', 'School of Information Systems and Technology Management', 0, 'Sydney', 0);", 
            "insert into Course (course_code, course_name, faculty_name, school_name, level, campus, is_archived) values ('INFS2608', 'Database Management & Big Data Infrastructures', 'UNSW Business School', 'School of Information Systems and Technology Management', 0, 'Sydney', 0);", 
            "insert into Course (course_code, course_name, faculty_name, school_name, level, campus, is_archived) values ('INFS2609', 'Programming for Business', 'UNSW Business School', 'School of Information Systems and Technology Management', 0, 'Sydney', 0);" 
        };
        
        for (String s : courses) {
            st.execute(s);
        }
        
        // insert users
        
        String user = "insert into users (username, password) values ('admin','passWord!')";
        st.execute(user);
        
        // other stuff
        
        PreparedStatement pst2 = Database.getSharedConnection().prepareStatement("INSERT INTO "
                + "module (module_name, module_description, is_archived) "
                + "VALUES (?,?,?)");
        pst2.setString(1, "Importance of Managing Information Systems");
        pst2.setString(2, "Goals of Information Security");
        pst2.setInt(3, 0);
        
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
                + "CoursesHaveModules (module_name, course_code, sequence_no) "
                + "VALUES (?,?,?)");
        pst7.setString(1, "Importance of Managing Information Systems");
        pst7.setString(2, "ACCT2511");
        pst7.setInt(3, 1);
        
        //insert statement for second module
        PreparedStatement pst8 = Database.getSharedConnection().prepareStatement("INSERT INTO "
                + "module (module_name, module_description, is_archived) "
                + "VALUES (?,?,?)");
        pst8.setString(1, "Second Module for Testing");
        pst8.setString(2, "Arbitrary Description");
        pst8.setInt(3, 0);
        
        pst2.executeUpdate();
        pst4.executeUpdate();
        pst5.executeUpdate();
        pst6.executeUpdate();
        pst7.executeUpdate();
        pst8.executeUpdate();
        
        st.close();
        Database.closeConnection();
    }
}
