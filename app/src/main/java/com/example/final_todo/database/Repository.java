package com.example.final_todo.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.final_todo.model.Category;
import com.example.final_todo.model.Register;
import com.example.final_todo.model.Todo;

import java.util.Date;
import java.util.List;

public class Repository {
    private CategoryDao categoryRepostiroy;

    private TodoDao todoRepository;
    private UserDao userRepository;

    public Repository(AppDatabase appDatabase) {
        this.categoryRepostiroy = appDatabase.categoryDao();
        this.todoRepository = appDatabase.todoDao();
        this.userRepository = appDatabase.userDao();
    }

    public void insertCategory(Category category) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            categoryRepostiroy.insertCategory(category);
        });
    }

    public void updateCategory(Category category) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            categoryRepostiroy.updateCategory(category);
        });
    }

    public void deleteCategory(Category category) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            categoryRepostiroy.deleteCategory(category);
        });
    }


    public void DeleteAllCategory() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            categoryRepostiroy.DeleteAllCategory();
        });
    }

    public void Delete(Todo todo) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            todoRepository.delete(todo);
        });
    }

    public void updateTodo(Todo todo) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            todoRepository.updateTodo(todo);
        });
    }

    public LiveData<Category> loadCategoryName(Integer catId) {
        return categoryRepostiroy.loadCategoryNameById(catId);
    }

    public LiveData<List<Category>> loadAllCategory() {
        return categoryRepostiroy.loadAllCategory();
    }

    public LiveData<List<Category>> loadCategoryById(Integer categoryId) {
        return categoryRepostiroy.loadCategoryById(categoryId);
    }

    public void insertTodo(Todo todo) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            todoRepository.insert(todo);
        });
    }

    public void deleteTodo() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            todoRepository.deleteAll();
        });
    }

    public LiveData<List<Todo>> loadAllTodo(Integer categoryId) {
        return todoRepository.loadTodoByCategoryId(categoryId);
    }

    public LiveData<Category> loadCategoryIdByName(String categoryName) {
        return categoryRepostiroy.loadCategoryIdByName(categoryName);
    }

    public void deleteAllCompleted() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            todoRepository.deleteAllCompleted();
        });
    }

    public void completeTask(int todoId, Date completedOn) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            todoRepository.completeTask(todoId,completedOn);
        });
    }

    public LiveData<List<Todo>> getAlltodo() {
        return todoRepository.getAllTodo();
    }

    public LiveData<Register> ValidateUser(String username, String password) {
        return userRepository.validateUser(username, password);
    }

    public void insertUserDetails(Register register) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            userRepository.insertUserDetails(register);
        });
    }

    public LiveData<Todo> loadTodoById(int categoryId) {
        return todoRepository.loadTodoById(categoryId);
    }
    public void UpdateTodo(String newTitle, String newDescription, Date newTodoDate, boolean newIsCompleted, int newPriority, Integer newCategoryId, int todoId, Date completedOn) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            todoRepository.UpdateTodo( newTitle,  newDescription,  newTodoDate, newIsCompleted, newPriority, newCategoryId,todoId,completedOn);
        });
    }

    public void UpdateTodoList(String newTitle, String newDescription, Date newTodoDate, boolean newIsCompleted, int newPriority, Integer newCategoryId, int todoId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            todoRepository.UpdateTodoList( newTitle,  newDescription,  newTodoDate, newIsCompleted, newPriority, newCategoryId,todoId);
        });
    }


}
