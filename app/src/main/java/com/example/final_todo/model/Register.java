package com.example.final_todo.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class Register {
    @PrimaryKey(autoGenerate = true)
    private int userId;
    @ColumnInfo(name = "fullname")
    private String fullname;
    @ColumnInfo(name = "gmail")
    private String gmail;
    @ColumnInfo(name = "password")
    private String password;
    @ColumnInfo(name = "number")
    private String number;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Register{" +
                "fullname='" + fullname + '\'' +
                ", gmail='" + gmail + '\'' +
                ", password='" + password + '\'' +
                ", number='" + number + '\'' +
                '}';
    }

}



