package com.example.chanw.surveypsm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.UserManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chanw.surveypsm.Model.User;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    Button mRegister;
    EditText mEmail, mPassword, mConfirmPassword;
    User user;
    SessionManager sessionManager;

    ProgressDialog pdLoading;

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
                if(!mConfirmPassword.getText().toString().equals( mPassword.getText().toString())){
                    mConfirmPassword.setText("");
                    mPassword.setText("");
                    Snackbar.make(view, "Please ensure both Passwords are the same.", Snackbar.LENGTH_LONG).show();
                }else{
                    user = new User();
                    user.setEmail(mEmail.getText().toString());
                    user.setPassword(mPassword.getText().toString());
                    new Register().execute(user);
                    pdLoading = new ProgressDialog(RegisterActivity.this);
                    pdLoading.setMessage("Signing In...");
                    pdLoading.show();
                }
            }
        });
    }

    private class Register extends AsyncTask<User, Void, String> {

        @Override
        protected String doInBackground(User... users) {

            HttpHandler httpHandler = new HttpHandler();
            String url = getString(R.string.base_url) + "userRegister.php";

            JSONObject params = new JSONObject();
            try {
                params.put("Email", users[0].getEmail());
                params.put("Password", users[0].getPassword());
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return httpHandler.postServiceCall(url,params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pdLoading.dismiss();
            if(s.equals("true")){
                sessionManager = new SessionManager(getApplicationContext());
                sessionManager.createLoginSession(user.getEmail());

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }else{

                mEmail.setText("");
                mPassword.setText("");
                mConfirmPassword.setText("");

                Toast.makeText(getApplicationContext(), "Email has been registered before.", Toast.LENGTH_LONG).show();
            }
        }
    }

}
