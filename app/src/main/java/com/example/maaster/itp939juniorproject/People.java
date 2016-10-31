package com.example.maaster.itp939juniorproject;

import android.content.Intent;

/**
 * Created by Administrator on 19/9/2559.
 */
public class People {

    private String _id;
    private String name;
    private Intent age;
    private String content;
    private String fbname;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Intent getAge() {
        return age;
    }

    public void setAge(Intent age) {
        this.age = age;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFbname() {
        return fbname;
    }

    public void setFbname(String fbname) {
        this.fbname = fbname;
    }
}
