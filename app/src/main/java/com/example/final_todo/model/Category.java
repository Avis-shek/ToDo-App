package com.example.final_todo.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "category")
public class Category {
    @PrimaryKey(autoGenerate = true)
    private Integer categoryId;

    @ColumnInfo(name = "category")
    private String category;

    public Category() {
    }

    public Category(Integer selectedId, String s) {
        this.categoryId = selectedId;
        this.category = s;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return category;
    }
}
