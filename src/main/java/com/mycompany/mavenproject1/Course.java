/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import java.util.List;

/**
 *
 * @author Keane Local
 */

public class Course {
    // constant definitions for readability
    private static final int UNDERGRAD = 0;
    private static final int POSTGRAD = 1;
    
    private String courseCode;
    private String name;
    private String faculty;
    private String school;
    private int level;
    private String campus;
    private int isArchived;

    public Course(String courseCode, String name, String faculty, String school, int level, String campus, int isArchived) {
        this.courseCode = courseCode;
        this.name = name;
        this.faculty = faculty;
        this.school = school;
        this.level = level;
        this.campus = campus;
        this.isArchived = isArchived;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public int getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(int isArchived) {
        this.isArchived = isArchived;
    }
    
}
