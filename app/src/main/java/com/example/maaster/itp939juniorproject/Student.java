package com.example.maaster.itp939juniorproject;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Administrator on 11/10/2559.
 */


public class Student implements Parcelable{
    private String name;
    private String pass;
    private String id;



    private ArrayList<String> course;
    private ArrayList<String> section;

    @ParcelConstructor
    public Student(String name, String pass, String id) {
        this.name = name;
        this.pass = pass;
        this.id = id;
        course = new ArrayList<>();
        section = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public ArrayList<String> getCourse() {
        return course;
    }

    public void setCourse(ArrayList<String> course) {
        this.course = course;
    }

    public ArrayList<String> getSection() {
        return section;
    }

    public void setSection(ArrayList<String> section) {
        this.section = section;
    }

    public static Creator<Student> getCREATOR() {
        return CREATOR;
    }

    public void addCourse(String c) {
        course.add(c);
    }

    public void addSection (String s) {
        section.add(s);
    }

    protected Student(Parcel in) {
        name = in.readString();
        pass = in.readString();
        course = in.createStringArrayList();
        section = in.createStringArrayList();
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(pass);
        parcel.writeStringList(course);
        parcel.writeStringList(section);
    }
}
