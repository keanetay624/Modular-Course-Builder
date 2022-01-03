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
        
        // data strings for each table
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
        
        String[] outcomes = {
            "insert into ModuleLearningOutcomes (module_name, mlo_name, mlo_description, sequence_no, is_archived) values ('Recording Economic Events Through Accounting Systems', 'Outcome name', 'Outcome desc', 1, 0);",
             
        };
        
        String[] modules = {
            "insert into Module (module_name, module_description, is_archived) values ('Recording Economic Events Through Accounting Systems', 'Topics covered in this course will include record keeping, double entry system, accounts receivable, inventories, non-current assets, liabilities, revenues, expenses, balance sheet and income statement preparation, cash flow statements, and accounting policy choice.', 0);",
            "insert into Module (module_name, module_description, is_archived) values ('Critical Appreciation of Important Issues in Accounting Theory and Practice', 'Topics covered in this course will include record keeping, double entry system, accounts receivable, inventories, non-current assets, liabilities, revenues, expenses, balance sheet and income statement preparation, cash flow statements, and accounting policy choice.', 0);",
            "insert into Module (module_name, module_description, is_archived) values ('Recording Business Transactions using Accounting Software', 'Topics covered in this course will include record keeping, double entry system, accounts receivable, inventories, non-current assets, liabilities, revenues, expenses, balance sheet and income statement preparation, cash flow statements, and accounting policy choice.', 0);",
            "insert into Module (module_name, module_description, is_archived) values ('Familiarity with Institutional structures that affect the practice of accounting', 'Topics covered in this course will include record keeping, double entry system, accounts receivable, inventories, non-current assets, liabilities, revenues, expenses, balance sheet and income statement preparation, cash flow statements, and accounting policy choice.', 0);",
             
        };
        
        String[] coursesHaveModules = {
            "insert into CoursesHaveModules (module_name, course_code, sequence_no) values ('Recording Economic Events Through Accounting Systems', 'ACCT2511', 1);"
        };
        
        String[] users = {
            "insert into users (username, password) values ('admin','passWord!')"
        };
        
        String[] sections = {
            "insert into section (module_name, section_name, section_description, sequence_no, is_archived) values ('Recording Economic Events Through Accounting Systems','Types of Accounting Systems', 'This section describes different types of Accounting Systems!', 1, 0)"
        };
        
        String[] resources = {
            "insert into resource (section_name, resource_name, resource_ext, is_archived) values ('Types of Accounting Systems','Lecture Notes', '.pdf', 0)"
        };
        
        // for loops for inserting into database.
        for (String s : courses) {st.execute(s);}
        for (String s : modules) {st.execute(s);}
        for (String s : outcomes) {st.execute(s);}
        for (String s : coursesHaveModules) {st.execute(s);}
        for (String s : users) {st.execute(s);}
        for (String s : sections) {st.execute(s);}
        for (String s : resources) {st.execute(s);}
        
        st.close();
        Database.closeConnection();
    }
}
