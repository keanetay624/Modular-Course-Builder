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
public class Section {
    private String id;
    private Module sModule;
    private int sequence;
    private String name;
    private String desc;
    private int isArchived;

    public Section(String id, Module sModule, String name, String desc, int sequence, int isArchived) {
        this.id = id;
        this.sModule = sModule;
        this.name = name;
        this.desc = desc;
        this.sequence = sequence;
        this.isArchived = isArchived;
    }

    public int getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(int isArchived) {
        this.isArchived = isArchived;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Module getsModule() {
        return sModule;
    }

    public void setsModule(Module sModule) {
        this.sModule = sModule;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
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
    
    
}
