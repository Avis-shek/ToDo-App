package com.example.final_todo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.final_todo.database.AppDatabase;
import com.example.final_todo.database.Repository;
import com.example.final_todo.model.Todo;

import java.util.Date;
import java.util.List;

public class TodoViewModel extends AndroidViewModel {
    private LiveData<List<Todo>> todoList;
    Repository repository;

    public TodoViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        repository = new Repository(appDatabase);
    }

    public void saveTodo(Todo todo) {
        repository.insertTodo(todo);
    }

    public void deleteTodo() {
        repository.deleteTodo();
    }

    public void deleteAllCompleted() {
        repository.deleteAllCompleted();
    }

    public void delete(Todo todo) {
        repository.Delete(todo);
    }

    public void completeTask(int todoId,Date completedOn) {
        repository.completeTask(todoId, completedOn);
    }

    public void updateTodo(Todo todo) {
        repository.updateTodo(todo);
    }

    public LiveData<List<Todo>> getTodoListByCategoryId(Integer categoryId) {
        todoList = repository.loadAllTodo(categoryId);
        return todoList;
    }

    public LiveData<List<Todo>> getAllTodoList() {
        todoList = repository.getAlltodo();
        return todoList;
    }

    public LiveData<Todo> loadTodoById(int todoId) {
        return repository.loadTodoById(todoId);
    }

    public void UpdateNewTodo(String newTitle, String newDescription, Date newTodoDate, boolean newIsCompleted, int newPriority, Integer newCategoryId, int todoId, Date completedOn) {
        repository.UpdateTodo( newTitle,  newDescription,  newTodoDate, newIsCompleted, newPriority, newCategoryId,todoId, completedOn);
    }

    public void UpdateNewTodoList(String newTitle, String newDescription, Date newTodoDate, boolean newIsCompleted, int newPriority, Integer newCategoryId, int todoId) {
        repository.UpdateTodoList( newTitle,  newDescription,  newTodoDate, newIsCompleted, newPriority, newCategoryId,todoId);
    }

}
