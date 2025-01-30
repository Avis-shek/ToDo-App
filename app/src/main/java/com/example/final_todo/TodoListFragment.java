package com.example.final_todo;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.final_todo.adaptor.CategoryAdaptor;
import com.example.final_todo.adaptor.TodoAdaptor;
import com.example.final_todo.model.Category;
import com.example.final_todo.model.Todo;
import com.example.final_todo.viewmodel.CategoryViewModel;
import com.example.final_todo.viewmodel.TodoViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class TodoListFragment extends Fragment implements TodoAdaptor.OnTaskClickListner {

    TodoViewModel todoViewModel;
    CategoryViewModel categoryViewModel;
    RecyclerView todoRecyclerView;
    TodoAdaptor todoAdaptor;

    FloatingActionButton flotingBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_list, container, false);
        todoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        todoRecyclerView = view.findViewById(R.id.activity_todo_list_rv_todo);
        todoAdaptor = new TodoAdaptor(this::onItemClick);

        Intent intent = getActivity().getIntent();
        int fk_category_id = intent.getIntExtra("categoryId", -1);
        todoViewModel.getTodoListByCategoryId(fk_category_id).observe(getActivity(), (Observer<List<Todo>>) todos -> {
            categoryViewModel.loadCategoryName(fk_category_id).observe(getActivity(), (Observer<Category>) categoryName -> {
                if (categoryName != null) {
                    todoAdaptor.setTodoList(todos);
                    todoRecyclerView = view.findViewById(R.id.activity_todo_list_rv_todo);
                    todoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    todoRecyclerView.setAdapter(todoAdaptor);
                } else {
                    todoViewModel.getAllTodoList().observe(getActivity(),
                            todos2 -> {
                                todoAdaptor.setTodoList(todos2);
                                todoRecyclerView = view.findViewById(R.id.activity_todo_list_rv_todo);
                                todoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                todoRecyclerView.setAdapter(todoAdaptor);
                            });
                }
            });
        });
        flotingBtn = view.findViewById(R.id.floatbtn);
        flotingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TodoListActivity todo = (TodoListActivity) getActivity();
                todo.replaceFragmentTodo();
            }
        });
        getActivity().getIntent().putExtra("categoryId", -1);

        // ItemTouchHelper for swipe and drag events
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // Not needed for drag events
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Todo todo = todoAdaptor.todoList.get(position); // Access todo directly
                switch (direction) {
                    case ItemTouchHelper.RIGHT:
                        // Handle swipe right (delete)
                        if (todoAdaptor.updateTimeRunnable != null) {
                            todoAdaptor.handler.removeCallbacks(todoAdaptor.updateTimeRunnable);
                        }
                        todoViewModel.delete(todo);
                        Toast.makeText(requireContext(), "Todo deleted", Toast.LENGTH_SHORT).show();
                        todoAdaptor.removeTodoAt(position); // Remove the item from the adapter immediately
                        break;
                    case ItemTouchHelper.LEFT:
                        if (todoAdaptor.updateTimeRunnable != null) {
                            todoAdaptor.handler.removeCallbacks(todoAdaptor.updateTimeRunnable);
                        }
                        // Handle swipe left (complete)
                        if (!todo.isCompleted()) {
                            Date completedOn = new Date();
                            todo.setCompletedOn(completedOn);
                            todoViewModel.completeTask(todo.getTodoId(), completedOn);
                            Toast.makeText(requireContext(), "Todo Status Updated.", Toast.LENGTH_SHORT).show();
                            todoAdaptor.notifyItemChanged(position); // Notify the adapter that the item at the position has changed
                        } else {
                            Toast.makeText(requireContext(), "Todo Status already Updated.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                todoRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                todoRecyclerView.setAdapter(todoAdaptor);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    Drawable deleteIcon = ContextCompat.getDrawable(requireContext(), android.R.drawable.ic_delete); // Replace with your delete icon drawable


                    int iconMargin = (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;

                    if (dX > itemView.getWidth() / 7) {
                        // Swiping right: Paint red and show the icon
//                        Paint paint = new Paint();
//                        paint.setColor(Color.parseColor(""));
//                        c.drawRect(itemView.getLeft() + dX, itemView.getTop(), itemView.getLeft(), itemView.getBottom(), paint);

                        // Adjust the delete icon position
                        int deleteIconLeft = itemView.getLeft() + 20;
                        int deleteIconRight = deleteIconLeft + deleteIcon.getIntrinsicWidth();
                        int deleteIconTop = itemView.getTop() + (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                        int deleteIconBottom = deleteIconTop + deleteIcon.getIntrinsicHeight();
                        deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
                        deleteIcon.draw(c);

                        // Draw the text "Delete"
                        String text = "Delete";
                        Paint textPaint = new Paint();
                        textPaint.setColor(Color.WHITE);
                        textPaint.setTextSize(40);
                        float textX = deleteIconRight + 20; // Adjust the X position as needed
                        float textY = itemView.getTop() + itemView.getHeight() / 2 + Math.abs(textPaint.descent() + textPaint.ascent()) / 2;
                        c.drawText(text, textX, textY, textPaint);
                    }
                } else if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && dX < 0) {
                    // Display update icon when swiped left
                    View itemView = viewHolder.itemView;
                    Drawable updateIcon = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_check_box_24); // Replace with your update icon drawable

                    if (Math.abs(dX) > itemView.getWidth() / 7) {
                        // Adjust the update icon position
                        int updateIconRight = itemView.getRight() - 20;

                        int updateIconLeft = updateIconRight - updateIcon.getIntrinsicWidth();
                        int updateIconTop = itemView.getTop() + (itemView.getHeight() - updateIcon.getIntrinsicHeight()) / 2;
                        int updateIconBottom = updateIconTop + updateIcon.getIntrinsicHeight();
                        updateIcon.setBounds(updateIconLeft, updateIconTop, updateIconRight, updateIconBottom);
                        updateIcon.draw(c);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
//            private boolean hasExecuted = false;
//            @Override
//            public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
//                int position = viewHolder.getAdapterPosition();
//                Todo todo = todoAdaptor.getTodoList().get(position);
//                if (todo.isCompleted() && !hasExecuted) {
//                    hasExecuted = true;
//                    Log.d("TodoListFragment","Triggered!");
//                    Toast.makeText(getContext(),"Already completed!",Toast.LENGTH_SHORT).show();
//                    return makeMovementFlags(0, ItemTouchHelper.RIGHT); // Prevent left swipe
//                }
//                return super.getSwipeDirs(recyclerView, viewHolder);
//            }
        });
        itemTouchHelper.attachToRecyclerView(todoRecyclerView);
        return view;
    }

    @Override
    public void onItemClick(int position) {
        Todo todo = todoAdaptor.todoList.get(position);
        if (todo.isCompleted()) {
            Toast.makeText(getContext(), "Can not update completed!", Toast.LENGTH_SHORT).show();
        } else {
            if (todoAdaptor.updateTimeRunnable != null) {
                todoAdaptor.handler.removeCallbacks(todoAdaptor.updateTimeRunnable);
            }
            int todoId = todo.getTodoId();
            startActivity(new Intent(getActivity(), TodoListActivity.class).putExtra("TodoId", todoId));
        }
    }
}