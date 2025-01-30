package com.example.final_todo;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.final_todo.model.Register;
import com.example.final_todo.viewmodel.RegisterViewModel;

public class RegisterFragment extends Fragment {
    RegisterViewModel viewModel;
    public EditText fullname;
    public EditText RconfirmPassword;
    public EditText Rpassword;
    public EditText gmail;
    public CheckBox termsConditoon;
    public TextView loginTxt;
    public Button RegisterBtn;
    public EditText number;

    private OnFragmentDisplayedListener callback;

    public interface OnFragmentDisplayedListener {
        void onFragmentDisplayed();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (OnFragmentDisplayedListener) context;
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        //setContentView(R.layout.fragment_register);
        viewModel = new RegisterViewModel(getActivity().getApplication());
        Rpassword = view.findViewById(R.id.register_activity_password);
        RconfirmPassword = view.findViewById(R.id.register_activity_Cpassword);
        fullname = view.findViewById(R.id.register_activity_username);
        gmail = view.findViewById(R.id.register_activity_gmail);
        termsConditoon = view.findViewById(R.id.TermcheckBox);
        loginTxt = view.findViewById(R.id.login_link);
        RegisterBtn = view.findViewById(R.id.register_btn);
        number = view.findViewById(R.id.register_phoneNumber);

        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAllInput();
            }
        });

        loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.replaceFragmentLogin();
            }
        });
        // Name Validation
        fullname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // This method is called when the text is changed
                String FullName = fullname.getText().toString();
                if (isValidFullName(FullName)) {
                    fullname.setError(null);
                } else {
                    fullname.setError("Please enter a valid full name");
                }
            }

            private boolean isValidFullName(String fullName) {
                String regexPattern = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$";
                return fullName.matches(regexPattern);
            }

            ;

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Gmail validation
        gmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is called before the text is changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // This method is called when the text is changed
                String email = gmail.getText().toString();
                if (isValidEmail(email)) {
                    gmail.setError(null);
                } else {
                    gmail.setError("Please enter a valid email address");
                }
            }

            private boolean isValidEmail(String email) {
                String regexPattern = "[a-zA-Z0-9._-]+@gmail\\.com";
                return email.matches(regexPattern);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This method is called after the text is changed
            }
        });


// Set a TextWatcher on the EditText Number
        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is called before the text is changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // This method is called whenever the text is changed
                String Number = number.getText().toString();

                // Check if the text is empty or not
                if (Number.isEmpty()) {
                    number.setError("This field is required");
                } else {
                    // Check if the text contains only digits
                    if (!TextUtils.isDigitsOnly(Number)) {
                        number.setError("Only numbers are allowed");
                    } else {
                        // Check if the length of the text is 10
                        if (Number.length() != 10) {
                            number.setError("Length must be 10 digits");
                        } else {
                            number.setError(null);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This method is called after the text is changed
            }
        });

        // password validation
        TextWatcher passwordTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = Rpassword.getText().toString();
                String confirmPassword = RconfirmPassword.getText().toString();

                if (!password.equals(confirmPassword)) {
                    // Passwords do not match
                    RconfirmPassword.setError("Passwords do not match");
                } else {
                    // Passwords match
                    RconfirmPassword.setError(null);
                }
            }
        };

        Rpassword.addTextChangedListener(passwordTextWatcher);
        RconfirmPassword.addTextChangedListener(passwordTextWatcher);
        return view;
    }


    public void saveAllInput() {
        String Fullname = this.fullname.getText().toString();
        String Gmail = this.gmail.getText().toString();
        String Password = this.Rpassword.getText().toString();
        String PhoneNum = this.number.getText().toString();
        Boolean TermsCon = this.termsConditoon.isChecked();

        if (Fullname.isEmpty()) {
            fullname.setError("This field is required");
        }

        if (Gmail.isEmpty()) {
            gmail.setError("This field is required");
        }

        if (Password.isEmpty()) {
            Rpassword.setError("This field is required");
        }

        if (Password.isEmpty()) {
            Rpassword.setError("This field is required");
        }


        if (RconfirmPassword.getText().toString().isEmpty()) {
            RconfirmPassword.setError("This field is required");
        }

        if (PhoneNum.isEmpty()) {
            number.setError("This field is required");
        }

        if (TermsCon == false) {
            termsConditoon.setError("User aggrement needs to be checked");
        }

        if (!Fullname.isEmpty() && !Gmail.isEmpty() && !Password.isEmpty() && TermsCon != false) {
            if (TermsCon == true) {
                Register register = new Register();
                register.setFullname(Fullname);
                register.setGmail(Gmail);
                register.setPassword(Password);
                register.setNumber(PhoneNum);

                viewModel.saveUser(register);
                Toast.makeText(getActivity(), "Registered Successfully", Toast.LENGTH_LONG).show();
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.replaceFragmentLogin();
            }
        } else {
            Toast.makeText(getActivity(), "Fields are empty", Toast.LENGTH_LONG).show();
        }
    }
}
