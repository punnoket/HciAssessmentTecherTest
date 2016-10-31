package com.example.maaster.itp939juniorproject;

/**
 * Created by Administrator on 13/10/2559.
 */
public class Course {
    private String name;
    private String section;

    public Course(String section, String name) {
        this.section = section;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
