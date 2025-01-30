package com.example.final_todo.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "todoHistory")
public class TodoHistory {
    //@ColumnInfo(name = "todoId")
    @PrimaryKey()
    private int todoId;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "todoDate")
    private Date todoDate;

    @ColumnInfo(name = "todoCompletedDate")
    private Date todoCompletedDate;

    @ColumnInfo(name = "isCompleted")
    private boolean isCompleted;

    @ColumnInfo(name = "priority")
    private int priority;
    // 0 = high, 1=medium, 2= low

    @ColumnInfo(name = "categoryId")
    private Date createdOn;

}
