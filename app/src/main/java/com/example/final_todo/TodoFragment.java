package com.example.final_todo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.final_todo.model.Category;
import com.example.final_todo.model.Todo;
import com.example.final_todo.viewmodel.CategoryViewModel;
import com.example.final_todo.viewmodel.TodoViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TodoFragment extends Fragment {

    Spinner categoryDropDownList;
    EditText txtTodoDate, txtTitle, txtDescription;
    RadioGroup rgPriority;
    CheckBox chkComlete;
    Button btnSave;
    CategoryViewModel categoryViewModel;
    TextView categoryTxt;
    RadioButton radioBtnLow, radioBtnMedium, radioBtnHigh;

    TodoViewModel todoViewModel;
    Integer todoId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        categoryViewModel = new ViewModelProvider(getActivity()).get(CategoryViewModel.class);
        todoViewModel = new ViewModelProvider(getActivity()).get(TodoViewModel.class);
        categoryDropDownList = view.findViewById(R.id.fragment_todo_cbo_category);
        txtTodoDate = view.findViewById(R.id.fragment_todo_txt_date);
        txtTitle = view.findViewById(R.id.fragment_todo_txt_title);
        txtDescription = view.findViewById(R.id.fragment_todo_txtDescription);
        rgPriority = view.findViewById(R.id.fragment_todo_rg_priority);
        chkComlete = view.findViewById(R.id.fragment_todo_chk_complete);
        btnSave = view.findViewById(R.id.fragment_todo_btn_Save);
        categoryTxt = view.findViewById(R.id.categoryTxt);
        radioBtnLow = view.findViewById(R.id.fragment_todo_rb_low);
        radioBtnMedium = view.findViewById(R.id.fragment_todo_rb_mid);
        radioBtnHigh = view.findViewById(R.id.fragment_todo_rb_high);

        categoryViewModel.getCategoryList().observe(getViewLifecycleOwner(), categories -> {
            setCategorySpinner(categories);
        });

        txtTodoDate.setOnClickListener(v -> showDateDialog());
        btnSave.setOnClickListener(v -> saveData());

        Intent intent = getActivity().getIntent();
        if (intent != null) {
            todoId = intent.getIntExtra("TodoId", -1);
            intent.removeExtra("TodoId");
            if (todoId != -1) {
                todoViewModel.loadTodoById(todoId).observe(getActivity(), todo -> {
                    if (todo.getCategoryId() == null) {
                        btnSave.setEnabled(false);
                        Toast.makeText(getActivity(), "No category found for this task", Toast.LENGTH_LONG).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                TodoListActivity mainActivity = (TodoListActivity) getActivity();
                                mainActivity.replaceFragmentTodoList();
                            }
                        }, 2000);
                        return;
                    } else {
                        txtTitle.setText(todo.getTitle());

                        txtDescription.setText(todo.getDescription());
                        int selectedCategoryId = todo.getCategoryId();
                        ArrayAdapter<Category> adapter = (ArrayAdapter<Category>) categoryDropDownList.getAdapter();
                        int position = -1;
                        for (int i = 0; i < adapter.getCount(); i++) {
                            Category category = adapter.getItem(i);
                            if (category.getCategoryId() == selectedCategoryId) {
                                position = i;
                                break;
                            }
                        }
                        if (position != -1) {
                            categoryDropDownList.setSelection(position);
                        }

                        Date date = todo.getTodoDate();
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        String dateString = format.format(date);
                        txtTodoDate.setText(dateString);

                        switch (todo.getPriority()) {
                            case 0:
                                radioBtnHigh.setChecked(true);
                                break;
                            case 1:
                                radioBtnMedium.setChecked(true);
                                break;
                            case 2:
                                radioBtnLow.setChecked(true);
                                break;
                        }
                        if (todo.isCompleted()) {
                            chkComlete.setChecked(true);
                        }

                        btnSave.setText("Update");
                    }
                });
            }
        }
        return view;
    }

    private void setCategorySpinner(List<Category> categories) {
        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, categories);
        categoryDropDownList.setAdapter(adapter);
    }

    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int selectedHour = calendar.get(Calendar.HOUR_OF_DAY);
                int selectedMinute = calendar.get(Calendar.MINUTE);
                // Create a TimePickerDialog to choose the time
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // The selected time
                                String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                                txtTodoDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year + " " + selectedTime);
                                // Update the EditText with the selected time
                                //timeEditText.setText(selectedTime);
                            }
                        }, selectedHour, selectedMinute, false);
                // Show the TimePickerDialog
                timePickerDialog.show();
            }
        }, year, month, day
        );
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void saveData() {
        String title = txtTitle.getText().toString();
        String descripton = txtDescription.getText().toString();
        String todoDate = txtTodoDate.getText().toString();
        Category category = (Category) categoryDropDownList.getSelectedItem();
        int checkedRadio = rgPriority.getCheckedRadioButtonId();

        if (title.isEmpty()) {
            txtTitle.setError("Title can not be empty");
        }

        if (descripton.isEmpty()) {
            txtDescription.setError("Description can not be empty");
        }

        if (todoDate.isEmpty()) {
            txtTodoDate.setError("Date can not be empty");
        }

        if (category == null) {
            categoryTxt.setError("Select one category");
        }

        int priority = 3;
        switch (checkedRadio) {
            case R.id.fragment_todo_rb_high:
                priority = 0;
                break;
            case R.id.fragment_todo_rb_mid:
                priority = 1;
                break;
            case R.id.fragment_todo_rb_low:
                priority = 2;
                break;
            default:
                priority = 3;
                radioBtnLow.setError("Select one radio button");
        }

        Date todoDateOn = null;
        if (!todoDate.isEmpty()) {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            try {
                todoDateOn = formatter.parse(todoDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        Date createdOn = new Date();
        boolean isComplete = chkComlete.isChecked();
        if (!title.isEmpty() && !descripton.isEmpty() && todoDateOn != null && priority != 3) {
            Todo todo = new Todo(title, descripton, todoDateOn, isComplete, priority, category.getCategoryId(), createdOn);

            if (todoId == -1) {
                if(isComplete) {
                    Date completedOn = new Date();
                    todo.setCompletedOn(completedOn);
                }
                todoViewModel.saveTodo(todo);
                Toast.makeText(getActivity(), "Todo Saved", Toast.LENGTH_SHORT).show();
            } else {
                if(isComplete)
                {
                    Date completedOn = new Date();
                    todo.setCompletedOn(completedOn);
                    todoViewModel.UpdateNewTodo(title, descripton, todoDateOn, isComplete, priority, category.getCategoryId(), todoId,completedOn);
                }
                else{
                    todoViewModel.UpdateNewTodoList(title, descripton, todoDateOn, isComplete, priority, category.getCategoryId(), todoId);
                }

                Toast.makeText(getActivity(), "Todo Updated", Toast.LENGTH_SHORT).show();
            }
            TodoListActivity TodoListActivity = (TodoListActivity) getActivity();
            TodoListActivity.replaceFragmentTodoList();
        } else {
            Toast.makeText(getActivity(), "Fields are empty.", Toast.LENGTH_LONG).show();
        }
    }
}