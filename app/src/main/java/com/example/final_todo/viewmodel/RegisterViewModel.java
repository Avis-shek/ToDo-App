package com.example.final_todo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.final_todo.database.AppDatabase;
import com.example.final_todo.database.Repository;
import com.example.final_todo.model.Category;
import com.example.final_todo.model.Register;

public class RegisterViewModel extends AndroidViewModel {
    Repository repository;

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(application);

        repository = new Repository(appDatabase);
    }

    public void saveUser(Register register) {
        repository.insertUserDetails(register);
    }
}
