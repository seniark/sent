package com.example.proj.sent;

/**
 * Created by 11bkr on 12/10/2017.
 */

public class Tick {

    private String name;
    private String grade;

    public String getName() {return this.name; }
    public String getGrade() {return this.grade; }
    public void setName(String n) { this.name = n; }
    public void setGrade(String g) {this.grade = g;}


    public Tick(String name, String grade)
    {
        this.name = name;
        this.grade = grade;


    }

    public Tick()
    {
        this.name = "";
        this.grade = "";
    }

}
