package com.mycompany.mavenproject1;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Keane Local
 */
public class Outcome {
    private Module oModule;
    private String name;
    private String desc;
    private int sequence;
    private int isArchived;

    public Outcome(Module oModule, String name, String desc, int sequence, int isArchived) {
        this.oModule = oModule;
        this.name = name;
        this.desc = desc;
        this.sequence = sequence;
        this.isArchived = isArchived;
    }

    public Module getoModule() {
        return oModule;
    }

    public void setoModule(Module oModule) {
        this.oModule = oModule;
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

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(int isArchived) {
        this.isArchived = isArchived;
    }
    
    
    
}
