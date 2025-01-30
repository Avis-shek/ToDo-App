package com.example.final_todo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.final_todo.database.AppDatabase;
import com.example.final_todo.database.Repository;
import com.example.final_todo.model.Register;

public class LoginViewModel extends AndroidViewModel {
    Repository repository;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        repository = new Repository(appDatabase);
    }

    public LiveData<Register> ValidateUser(String username, String password) {
        return repository.ValidateUser(username, password);
    }
}
