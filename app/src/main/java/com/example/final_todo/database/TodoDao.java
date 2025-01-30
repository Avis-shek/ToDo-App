package com.example.final_todo.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.final_todo.model.Todo;

import java.util.Date;
import java.util.List;

@Dao
public interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Todo todo);

    @Delete
    void delete(Todo todo);

    @Update
    void updateTodo(Todo todo);

    @Query("delete from todo")
    void deleteAll();

    @Query("delete from todo where isCompleted=1")
    void deleteAllCompleted();

    @Query("update todo set isCompleted=1,completedOn = :completedOn where todoId = :todoId")
    void completeTask(int todoId,Date completedOn);

    @Query("select * from todo where todoId = :todoId")
    LiveData<Todo> loadTodoById(int todoId);


    @Query("select * from todo where categoryId = :categoryId")
    LiveData<List<Todo>> loadTodoByCategoryId(Integer categoryId);

    @Query("select * from todo")
    LiveData<List<Todo>> getAllTodo();

    @Query("update todo set title = :newTitle, description = :newDescription,todoDate = :newTodoDate, isCompleted = :newIsCompleted,priority = :newPriority, categoryId = :newCategoryId, completedOn = :completedOn  where todoId = :todoId ")
    void UpdateTodo(String newTitle, String newDescription, Date newTodoDate, boolean newIsCompleted, int newPriority, Integer newCategoryId, int todoId, Date completedOn);

    @Query("update todo set title = :newTitle, description = :newDescription,todoDate = :newTodoDate, isCompleted = :newIsCompleted,priority = :newPriority, categoryId = :newCategoryId where todoId = :todoId ")
    void UpdateTodoList(String newTitle, String newDescription, Date newTodoDate, boolean newIsCompleted, int newPriority, Integer newCategoryId, int todoId);
}
