package com.example.final_todo.adaptor;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.final_todo.R;
import com.example.final_todo.model.Todo;
import com.example.final_todo.viewmodel.TodoViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import pl.droidsonroids.gif.GifImageView;

public class TodoAdaptor extends RecyclerView.Adapter<TodoAdaptor.TodoView> {
    public List<Todo> todoList;
    TodoViewModel viewModel;
    private TodoAdaptor.OnTaskClickListner onTaskClickListner;
    public final Handler handler = new Handler();
    public Runnable updateTimeRunnable;

    public TodoAdaptor(TodoAdaptor.OnTaskClickListner onTaskClickListner) {
        this.onTaskClickListner = onTaskClickListner;
    }

    public List<Todo> getTodoList() {
        return todoList;
    }

    public void setTodoList(List<Todo> todoList) {
        this.todoList = todoList;
    }

    public void removeTodoAt(int position) {
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public TodoAdaptor(TodoViewModel todoViewModel) {
        this.viewModel = todoViewModel;
    }

    @NonNull
    @Override
    public TodoView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todoitemlayout, parent, false);
        TodoView todoView = new TodoView(view, onTaskClickListner);
        return todoView;
    }

    @Override
    public void onBindViewHolder(@NonNull TodoView holder, int position) {
        analyticalDateConversion(holder, holder.getAdapterPosition());

        holder.tvTitle.setText(todoList.get(position).getTitle());
        holder.tvDescription.setText(todoList.get(position).getDescription());
        int priority = todoList.get(position).getPriority();
        if (priority == 0) {
            holder.tvPriority.setText(R.string.todo_high);
            holder.tvPriority.setTextColor(Color.RED);
        } else if (priority == 1) {
            holder.tvPriority.setText(R.string.todo_medium);
            holder.tvPriority.setTextColor(Color.YELLOW);
        } else {
            holder.tvPriority.setText(R.string.todo_low);
            holder.tvPriority.setTextColor(Color.GREEN);
        }

        // Check the task status
        if (todoList.get(position).isCompleted()) {
            // If the task is completed, cross out the title
            holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            // If the task is not completed, remove the strike-through
            holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        if (todoList.get(position).isCompleted()) {
            holder.gifImageView.setVisibility(View.VISIBLE);
            // Load and display the GIF using Glide or any other method
            Glide.with(holder.itemView)
                    .asGif()
                    .load(R.drawable.check1)
                    .transition(new DrawableTransitionOptions().crossFade(3000)) // Set duration
                    .listener(new RequestListener<GifDrawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                            resource.setLoopCount(1);// Set loop count to 1
                            return false;
                        }
                    })
                    .into(holder.gifImageView);
        } else {
            holder.gifImageView.setVisibility(View.VISIBLE);
            // Load and display the GIF using Glide or any other method
            Glide.with(holder.itemView)
                    .asGif()
                    .load(R.drawable.redcross)
                    .transition(new DrawableTransitionOptions().crossFade(3000)) // Set duration
                    .listener(new RequestListener<GifDrawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                            resource.setLoopCount(1);// Set loop count to 1
                            return false;
                        }
                    })
                    .into(holder.gifImageView);
        }
    }
    public void analyticalDateConversion(TodoView holder, int position) {

        DateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy HH:mm");
        holder.tvDate.setText(dateFormat.format(todoList.get(position).getTodoDate()));

        // To calculate how much is left or taken to complete the task
        if (todoList.get(position).isCompleted()) {
            Date date1 = todoList.get(position).getCompletedOn();
            Date date2 = todoList.get(position).getCreatedOn();
            // Calculate the difference in milliseconds
            long differenceInMilliseconds = Math.abs(date1.getTime() - date2.getTime());
            // Calculate the difference in days
            long differenceInDays = TimeUnit.MILLISECONDS.toDays(differenceInMilliseconds);
            // Calculate the difference in hours
            long differenceInHours = TimeUnit.MILLISECONDS.toHours(differenceInMilliseconds) % 24;
            // Calculate the difference in minutes
            long differenceInMinutes = TimeUnit.MILLISECONDS.toMinutes(differenceInMilliseconds) % 60;
            long differenceInSeconds = TimeUnit.MILLISECONDS.toSeconds(differenceInMilliseconds) % 60;

            if (differenceInDays > 0) {
                holder.tvComplete.setText("completed in:\n" + differenceInDays + "d " + differenceInHours + "h " + differenceInMinutes + "m ");
            } else if (differenceInHours > 0) {

                holder.tvComplete.setText("completed in:\n" + differenceInHours + "h " + differenceInMinutes + "m ");
            } else if (differenceInMinutes > 0) {
                holder.tvComplete.setText("completed in:\n" + differenceInMinutes + "m  " + differenceInSeconds + "s");
            } else {
                holder.tvComplete.setText("completed in:\n" + differenceInSeconds + "s ");
            }
        }
        if (!todoList.get(position).isCompleted()) {
            if (todoList.get(position).getTodoDate().equals(new Date()) || todoList.get(position).getTodoDate().before(new Date())) {
                holder.tvComplete.setText("Date expired");
            } else if (!todoList.get(position).isCompleted()) {
                updateTimeRunnable = new Runnable() {
                    @Override
                    public void run() {
                        Date date1 = new Date();
                        Date date2 = todoList.get(position).getTodoDate();
                        if (todoList.get(position).getTodoDate().equals(new Date()) || todoList.get(position).getTodoDate().before(new Date())) {
                            holder.tvComplete.setText("Date expired");
                            return;
                        } else if (!todoList.get(position).isCompleted()) {
                            // Calculate the difference in milliseconds
                            long differenceInMilliseconds = Math.abs(date1.getTime() - date2.getTime());
                            // Calculate the difference in days
                            long differenceInDays = TimeUnit.MILLISECONDS.toDays(differenceInMilliseconds);
                            // Calculate the difference in hours
                            long differenceInHours = TimeUnit.MILLISECONDS.toHours(differenceInMilliseconds) % 24;
                            // Calculate the difference in minutes
                            long differenceInMinutes = TimeUnit.MILLISECONDS.toMinutes(differenceInMilliseconds) % 60;
                            long differenceInSeconds = TimeUnit.MILLISECONDS.toSeconds(differenceInMilliseconds) % 60;

                            if (differenceInDays > 0) {
                                holder.tvComplete.setText("remaining:\n" + differenceInDays + "d " + differenceInHours + "h " + differenceInMinutes + "m ");
                            } else if (differenceInHours > 0) {

                                holder.tvComplete.setText("remaining:\n" + differenceInHours + "h " + differenceInMinutes + "m ");
                            } else if (differenceInMinutes > 0) {
                                holder.tvComplete.setText("remaining:\n" + differenceInMinutes + "m " + differenceInSeconds + "s ");
                            } else {
                                holder.tvComplete.setText("remaining:\n" + differenceInSeconds + "s");
                            }


                            // Build the countdown display text
//                        String countdownText = "Remaining: ";
//                        if (differenceInDays > 0) {
//                            countdownText += differenceInDays + "d ";
//                        }
//
//                        countdownText += String.format("%02d:%02d:%02d", differenceInHours, differenceInMinutes, differenceInSeconds);
//                        // Update the TextView with the countdown text
//                        holder.tvComplete.setText(countdownText);
                        }
                        handler.postDelayed(this, 1000);
                    }
                };
                handler.post(updateTimeRunnable);
            }
        }
    }


    @Override
    public int getItemCount() {
        return (todoList == null ? 0 : todoList.size());
    }

    public class TodoView extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitle, tvDescription, tvComplete, tvDate, tvPriority;
        GifImageView gifImageView;
        TodoAdaptor.OnTaskClickListner onTaskClickListner;

        public TodoView(@NonNull View itemView, TodoAdaptor.OnTaskClickListner onTaskClickListner) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.todo_item_tv_title);
            tvDescription = (TextView) itemView.findViewById(R.id.todo_item_tv_description);
            tvComplete = (TextView) itemView.findViewById(R.id.todo_item_tv_complete);
            tvDate = (TextView) itemView.findViewById(R.id.todo_item_tv_date);
            tvPriority = (TextView) itemView.findViewById(R.id.todo_item_tv_prority);
            gifImageView = itemView.findViewById(R.id.id_completed_gif);

            this.onTaskClickListner = onTaskClickListner;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onTaskClickListner.onItemClick(getAdapterPosition());
        }
    }

    public interface OnTaskClickListner {
        void onItemClick(int position);
    }
}


