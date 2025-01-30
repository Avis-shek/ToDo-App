package com.example.final_todo;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.final_todo.adaptor.CategoryAdaptor;
import com.example.final_todo.model.Category;
import com.example.final_todo.model.Todo;
import com.example.final_todo.viewmodel.CategoryViewModel;

public class CategoryListFragment extends Fragment implements CategoryAdaptor.OnTaskClickListner {

    CategoryViewModel categoryViewModel;
    RecyclerView categoryRecyclerView;
    CategoryAdaptor categoryAdaptor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);
        categoryViewModel = new ViewModelProvider(getActivity()).get(CategoryViewModel.class);
        categoryRecyclerView = view.findViewById(R.id.category_recycler_view);
        categoryAdaptor = new CategoryAdaptor(this::onItemClick);
        categoryViewModel.getCategoryList().observe(getActivity(), categories -> {
            categoryAdaptor.setCategoryList(categories);
            categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            categoryRecyclerView.setAdapter(categoryAdaptor);
        });

        // ItemTouchHelper for swipe and drag events
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // Not needed for drag events
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Category category = categoryAdaptor.categoryList.get(position); // Access todo directly
                switch (direction) {
                    case ItemTouchHelper.RIGHT:
                        // Handle swipe right (delete)
                        categoryViewModel.deleteCategory(category);
                        Toast.makeText(requireContext(), "Category deleted", Toast.LENGTH_SHORT).show();
                        categoryAdaptor.removeCategoryAt(position); // Remove the item from the adapter immediately
                        break;

                    case ItemTouchHelper.LEFT:
                        Integer catId = category.getCategoryId();
                        startActivity(new Intent(getActivity(), MainActivity.class).putExtra("categoryId", catId));
                        break;
                }
                categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                categoryRecyclerView.setAdapter(categoryAdaptor);
            }
        });
        itemTouchHelper.attachToRecyclerView(categoryRecyclerView);
        return view;
    }

    @Override
    public void onItemClick(int position) {
        String text = String.valueOf(categoryAdaptor.getCategoryList().get(position));
        showCategoryTodos(text);
    }

    private void showCategoryTodos(String categoryName) {
        categoryViewModel.getCategoryIdByName(categoryName).observe(requireActivity(), (Observer<Category>) category -> {
            if (category != null) {
                startActivity(new Intent(getActivity(), TodoListActivity.class).putExtra("categoryId", category.getCategoryId()));
            }
        });
    }
}