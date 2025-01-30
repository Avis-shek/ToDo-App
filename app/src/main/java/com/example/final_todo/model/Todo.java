package com.example.final_todo.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;


import java.util.Date;

@Entity(foreignKeys = @ForeignKey(entity = Category.class,
        parentColumns = "categoryId",
        onDelete = ForeignKey.SET_NULL,
        childColumns = "categoryId"),
        indices = {@Index("categoryId")}
)

public class Todo {
    @PrimaryKey(autoGenerate = true)

    private int todoId;



    @ColumnInfo(name = "completedOn")
    Date completedOn;

    private String title;

    private String description;

    private Date todoDate;

    private boolean isCompleted;

    private int priority;
    // 0 = high, 1=medium, 2= low
    private Integer categoryId;

    private Date createdOn;

    public Todo() {

    }

    @Ignore
    public Todo(String title, String description, Date todoDate, boolean isCompleted, int priority, Integer categoryId, Date createdOn) {
        this.title = title;
        this.description = description;
        this.todoDate = todoDate;
        this.isCompleted = isCompleted;
        this.priority = priority;
        this.categoryId = categoryId;
        this.createdOn = createdOn;
    }

    public Date getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(Date completedOn) {
        this.completedOn = completedOn;
    }
    public int getTodoId() {
        return todoId;
    }

    public void setTodoId(int todoId) {
        this.todoId = todoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTodoDate() {
        return todoDate;
    }

    public void setTodoDate(Date todoDate) {
        this.todoDate = todoDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
}
