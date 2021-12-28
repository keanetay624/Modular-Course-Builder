/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModularCourseBuilder;

/**
 *
 * @author Keane Local
 */
public class Resource {
    private Section rSection;
    private String name;
    private String ext;

    public Resource(Section rSection, String name, String ext) {
        this.rSection = rSection;
        this.name = name;
        this.ext = ext;
    }
    
    public Section getrSection() {
        return rSection;
    }
    
    public void setrSection(Section rSection) {
        this.rSection = rSection;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
    
    
}
