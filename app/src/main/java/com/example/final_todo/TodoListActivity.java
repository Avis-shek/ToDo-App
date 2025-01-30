package com.example.final_todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.final_todo.adaptor.TodoAdaptor;
import com.example.final_todo.model.Todo;
import com.example.final_todo.viewmodel.RegisterViewModel;
import com.example.final_todo.viewmodel.TodoViewModel;

import java.util.List;

public class TodoListActivity extends AppCompatActivity {
    TodoViewModel viewModel;
    public Integer todoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        viewModel = new TodoViewModel(this.getApplication());

        Intent intent = getIntent();
        if (intent != null) {
            todoId = intent.getIntExtra("TodoId", -1);
            if (todoId != -1) {
                replaceFragmentTodo();
            }
        }
    }

    public void replaceFragmentTodo() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.activity_todo_fragment_container, TodoFragment.class, null)
                .addToBackStack("AddTodo")
                .setReorderingAllowed(true)
                .commit();
    }

    public void replaceFragmentTodoList() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.activity_todo_fragment_container, TodoListFragment.class, null)
                .addToBackStack("TodoList")
                .setReorderingAllowed(true)
                .commit();
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.todolist_navigation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_addTask:
                    replaceFragmentTodo();
                return true;
            case R.id.navigation_deleteTodo:
                viewModel.deleteTodo();
                replaceFragmentTodoList();
                Toast.makeText(this, "All Todo are Deleted.", Toast.LENGTH_LONG).show();
                return true;
            case R.id.navigation_deleteCtask:
                viewModel.deleteAllCompleted();
                Toast.makeText(this, "All Completed Todo are Deleted.", Toast.LENGTH_LONG).show();
                replaceFragmentTodoList();
                return true;
            case R.id.navigation_gotoCategoryList:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("activity", "dummy");
                startActivity(intent);
                return true;
            case R.id.navigation_logout:
                intent = new Intent(this, MainActivity.class);
                intent.putExtra("activity", "todoList");
                startActivity(intent);
                return true;
        }
        return true;
    }
}