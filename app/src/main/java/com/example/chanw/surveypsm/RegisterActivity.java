package com.example.chanw.surveypsm;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.chanw.surveypsm.Model.User;

public class RegisterActivity extends AppCompatActivity {

    Button mRegister;
    EditText mEmail, mPassword, mConfirmPassword;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmail = (EditText) findViewById(R.id.et_register_email);
        mPassword = (EditText) findViewById(R.id.et_register_password);
        mConfirmPassword = (EditText) findViewById(R.id.et_con_password);

        mRegister = (Button) findViewById(R.id.btn_register);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mConfirmPassword.getText().toString() != mPassword.getText().toString()){
                    mConfirmPassword.setText("");
                    mPassword.setText("");
                    Snackbar.make(view, "Both ensure both Passwords are the same.", Snackbar.LENGTH_SHORT).show();
                }else{
                    user = new User();
                    user.setEmail(mEmail.getText().toString());
                    user.setPassword(mPassword.getText().toString());
                    Snackbar.make(view, "Havent Do Register Logic. But u can do the UI first", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }
}
