package com.example.maaster.itp939juniorproject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 14/10/2559.
 */
public class Teacher {

    private String name;
    private ArrayList<Course> course;
    private Integer patr1, part2, part3;
    private Integer countCourse;
    private String imageName;

    public Teacher(ArrayList<Course> course, String name, Integer countCourse) {
        course = new ArrayList<>();
        this.name = name;
        this.countCourse =countCourse;
    }

    public Teacher() {
        course = new ArrayList<>();
    }

    public Integer getCountCourse() {
        return countCourse;
    }

    public void setCountCourse(Integer countCourse) {
        this.countCourse = countCourse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Course> getCourse() {
        return course;
    }

    public void setCourse(ArrayList<Course> course) {
        this.course = course;
    }

    public void addCourse(Course course) {
        this.course.add(course);
    }

    public Integer getPatr1() {
        return patr1;
    }

    public void setPatr1(Integer patr1) {
        this.patr1 = patr1;
    }

    public Integer getPart2() {
        return part2;
    }

    public void setPart2(Integer part2) {
        this.part2 = part2;
    }

    public Integer getPart3() {
        return part3;
    }

    public void setPart3(Integer part3) {
        this.part3 = part3;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
