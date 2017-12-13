package com.example.proj.sent;

/**
 * Created by 11bkr on 12/10/2017.
 */

public class Tick {

    private String name;
    private String grade;
    private String image_uri;


    public String getName() {return this.name; }
    public String getGrade() {return this.grade; }
    public String getImage_uri() {return this.image_uri;}
    public void setName(String n) { this.name = n; }
    public void setGrade(String g) {this.grade = g;}
    public void setImage_uri(String u){this.image_uri = u;}


    public Tick(String name, String grade, String image_uri)
    {
        this.name = name;
        this.grade = grade;
        this.image_uri = image_uri;
    }

    public Tick()
    {
        this.name = "";
        this.grade = "";
        this.image_uri= "";
    }

}
