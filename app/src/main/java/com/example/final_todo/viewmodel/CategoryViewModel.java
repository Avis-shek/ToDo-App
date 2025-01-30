package com.example.final_todo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.final_todo.database.AppDatabase;
import com.example.final_todo.database.Repository;
import com.example.final_todo.model.Category;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {
    Repository repository;
    private LiveData<List<Category>> categoryList;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(application);

        repository = new Repository(appDatabase);
        //categoryList = repository.loadAllCategory();
    }

    public void saveCategory(Category category) {
        repository.insertCategory(category);
    }

    public void deleteCategory(Category category) {
        repository.deleteCategory(category);
    }

    public void DeleteAllCategory() {
        repository.DeleteAllCategory();
    }

    public void UpdateCategory(Category category) {
        repository.updateCategory(category);
    }


    public LiveData<List<Category>> getCategoryList() {
        return repository.loadAllCategory();
    }

    public LiveData<Category> loadCategoryName(Integer catId) {
        return repository.loadCategoryName(catId);
    }

    public LiveData<Category> getCategoryIdByName(String categoryName) {
        return repository.loadCategoryIdByName(categoryName);
    }

}
