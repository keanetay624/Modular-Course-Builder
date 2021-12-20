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
public class Module {
//    TODO: Implement this class
    private String name;
    private String desc;
    private int isArchived;

    public Module(String name, String desc, int isArchived) {
        this.name = name;
        this.desc = desc;
        this.isArchived = isArchived;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(int isArchived) {
        this.isArchived = isArchived;
    }
}
