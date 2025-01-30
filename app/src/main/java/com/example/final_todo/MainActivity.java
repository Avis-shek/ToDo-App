package com.example.final_todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.example.final_todo.model.Category;
import com.example.final_todo.viewmodel.CategoryViewModel;
import com.example.final_todo.viewmodel.TodoViewModel;

public class MainActivity extends AppCompatActivity implements RegisterFragment.OnFragmentDisplayedListener {
    CategoryViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new CategoryViewModel(this.getApplication());
        Intent intent = getIntent();
        if(intent != null){
            Integer catId = intent.getIntExtra("categoryId",-1);
            if(catId != -1){
                replaceFragmentCategory();
            }
        }
        String value = intent.getStringExtra("activity");
        if (!TextUtils.isEmpty(value)) {
            if (value.equals("todoList")) {
                replaceFragmentLogin();
            }
        }
    }

    public void replaceFragmentCategoryList() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.activity_main_fragment_container, CategoryListFragment.class, null)
                .addToBackStack("FragmentCategoryList")
                .setReorderingAllowed(true)
                .commit();
    }


    public void replaceFragmentCategory() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        String tag = "FragmentCategory";
        CategoryFragment categoryFragment = new CategoryFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.activity_main_fragment_container, categoryFragment,tag)
                .addToBackStack("FragmentCategory")
                .setReorderingAllowed(true)
                .commit();
    }

    public void replaceFragmentRegister() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.activity_main_fragment_container, RegisterFragment.class, null)
                .addToBackStack("Category")
                .setReorderingAllowed(true)
                .commit();
    }

    public void replaceFragmentLogin() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.activity_main_fragment_container, LoginFragment.class, null)
                .addToBackStack("Category")
                .setReorderingAllowed(true)
                .commit();
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_newcategory:
                replaceFragmentCategory();
                return true;
            case R.id.main_menu_clear_category:

                CategoryViewModel categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
                categoryViewModel.DeleteAllCategory();
                replaceFragmentCategoryList();
                //Add toast
                return true;
            case R.id.main_menu_Go_To_TodoList:
                Intent intent = new Intent(this, TodoListActivity.class);
                startActivity(intent);
                return true;

            case R.id.main_menu_logout:
                SharedPreferences sharedPreferences = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("loginToken");
                editor.apply();
                replaceFragmentLogin();
//                Intent intent = new Intent(this, LoginFragment.class);
//                startActivity(intent);
                return true;
        }
        return true;
    }

    @Override
    public void onFragmentDisplayed() {
        // Hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.setStatusBarColor(Color.TRANSPARENT);
        // Hide the status bar
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}