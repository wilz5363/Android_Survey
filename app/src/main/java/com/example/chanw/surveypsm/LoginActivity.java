package com.example.chanw.surveypsm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chanw.surveypsm.Model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    private String TAG = LoginActivity.class.getSimpleName();

    private int REQUEST_EXIT = 0;
    private int REQUEST_OK = 1;
    SessionManager sessionManager;
    ProgressDialog pdLoading = null;
    Button mSignInBtn;
    EditText mEmailEt, mPasswordEt;
    TextView mRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailEt = (EditText) findViewById(R.id.et_email);
        mPasswordEt = (EditText) findViewById(R.id.et_password);
        mSignInBtn = (Button) findViewById(R.id.btn_signIn);
        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();
                user.setEmail(mEmailEt.getText().toString());
                user.setPassword(mPasswordEt.getText().toString());

                pdLoading = new ProgressDialog(LoginActivity.this);
                pdLoading.setMessage("Signing In...");
                pdLoading.show();

                new UserSignInAsync().execute(user);
            }
        });


        mRegister = (TextView) findViewById(R.id.tv_register);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(registerIntent,REQUEST_EXIT);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_EXIT){
            finish();
        }

    }

    class UserSignInAsync extends AsyncTask<User, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(User... users) {

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            HttpHandler httpHandler = new HttpHandler();

            String url = getString(R.string.base_url) + "getSignIn.php?email=" + users[0].getEmail() + "&password=" + users[0].getPassword();
            String jsonResult = httpHandler.makeServiceCall(url);

            Log.e(TAG, "Result from json:" + jsonResult);
            if (jsonResult != null) {

                try {
                    JSONObject jsonObject = new JSONObject(jsonResult);
                    Boolean found = jsonObject.getBoolean("result");

                    if (found) {
                        sessionManager = new SessionManager(getApplicationContext());
                        sessionManager.createLoginSession(users[0].getEmail());

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Log.e(TAG, "False");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e(TAG, "Cant make connection...");
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pdLoading.dismiss();
        }
    }


}
