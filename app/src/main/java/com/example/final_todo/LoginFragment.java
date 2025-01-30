package com.example.final_todo;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.final_todo.viewmodel.LoginViewModel;

public class LoginFragment extends Fragment {
    LoginViewModel viewModel;
    EditText txtUserName;
    EditText txtPassword;
    Button btnLogin;
    Button btnCance;
    TextView registerLink;

    private RegisterFragment.OnFragmentDisplayedListener callback;

    public interface OnFragmentDisplayedListener {
        void onFragmentDisplayed();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (RegisterFragment.OnFragmentDisplayedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentDisplayedListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        callback.onFragmentDisplayed();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        viewModel = new LoginViewModel(getActivity().getApplication());
        txtUserName = view.findViewById(R.id.login_activity_username);
        txtPassword = view.findViewById(R.id.login_activity_password);
        btnLogin = view.findViewById(R.id.login_activity_btn_login);
        btnCance = view.findViewById(R.id.login_activity_btn_cancel);
        registerLink = view.findViewById(R.id.register_link);

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.replaceFragmentRegister();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtUserName.getText().toString().isEmpty())
                {
                    txtUserName.setError("Username is required");
                }
                if(txtPassword.getText().toString().isEmpty())
                {
                    txtPassword.setError("Password is required");
                }

                if (!txtUserName.getText().toString().isEmpty() && !txtPassword.getText().toString().isEmpty()) {
                    viewModel.ValidateUser(txtUserName.getText().toString(), txtPassword.getText().toString()).observe(getViewLifecycleOwner(), user -> {
                        if (user != null) {
                            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("loginToken", "Loggedin");
                            editor.apply();
                            Toast.makeText(getActivity(), "Logged in", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getActivity(), TodoListActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), "Invalid username or password", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Fields are empty", Toast.LENGTH_LONG).show();
                }
            }
        }
        )
        ;
        return view;
    }
}