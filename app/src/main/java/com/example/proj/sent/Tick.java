package com.example.proj.sent;

/**
 * Created by 11bkr on 12/10/2017.
 */

public class Tick {

    private String name;
    private String grade;

    public String getName() {return this.name; }
    public String getGrade() {return this.grade; }

    public Tick(String name, String grade)
    {
        this.name = name;
        this.grade = grade;
    }

}
