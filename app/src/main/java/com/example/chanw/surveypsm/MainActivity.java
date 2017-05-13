package com.example.chanw.surveypsm;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chanw.surveypsm.Adapter.SurveyListAdapter;
import com.example.chanw.surveypsm.Model.Survey;
import com.example.chanw.surveypsm.Model.UserSessions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    SessionManager session;
    public static final String TAG = MainActivity.class.getSimpleName();

    ListView mSurveyListView;
    TextView mNoSurveyTextView;

    ArrayList<Survey> surveys;
    Survey survey;
    SurveyListAdapter adapter;

    RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(getApplicationContext());
        if (!session.isLoggedIn()) {
            session.checkLogin();
            finish();
        }

        layout = (RelativeLayout) findViewById(R.id.content_main_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSurveyListView = (ListView) findViewById(R.id.lv_survey_list);
        mSurveyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                survey = surveys.get(position);
//                Snackbar.make(view, survey.getSurveyName()+"\n"+survey.getDescription(),Snackbar.LENGTH_LONG).setAction("No action", null).show();

                UserSessions userSessions = new UserSessions();
                userSessions.setEmail(session.getUserEmail());
                userSessions.setSurveyId(survey.getId());
                new CreateUserSession().execute(userSessions);



            }
        });


        mNoSurveyTextView = (TextView) findViewById(R.id.tv_no_survey);

        surveys = new ArrayList<>();
        new GetSurvey().execute();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        adapter.notifyDataSetChanged();
//    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        adapter.notifyDataSetChanged();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            finish();
            session.logoutUser();

        }

        return super.onOptionsItemSelected(item);
    }

    class GetSurvey extends AsyncTask<String, Void, Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            HttpHandler httpHandler = new HttpHandler();
            String url = getString(R.string.base_url)+"getSurveys.php";
            JSONObject params = new JSONObject();
            try {
                params.put("Email", session.getUserEmail());
            } catch (JSONException e) {
                e.printStackTrace();
            }


            String jsonResultString = httpHandler.postServiceCallWithReturnJson(url, params);


            Log.e(TAG, "Result from getSurvey.php:"+jsonResultString);
            try {
                JSONObject jsonObject = new JSONObject(jsonResultString);
                Log.e(TAG,"Result: " +jsonObject.getBoolean("result"));

                if(jsonObject.getBoolean("result")){
                    JSONArray jsonArraySurvey = jsonObject.getJSONArray("survey");
                    for(int count = 0; count<jsonArraySurvey.length(); count++){
                        JSONObject jsonSurvey = jsonArraySurvey.getJSONObject(count);
                        Survey survey = new Survey();
                        survey.setId(jsonSurvey.getInt("ID"));
                        survey.setSurveyName(jsonSurvey.getString("SurveyName"));
                        survey.setDescription(jsonSurvey.getString("SurveyDesc"));
                        survey.setExpiryDate(Date.valueOf(jsonSurvey.getString("ExpiryDate")));

                        surveys.add(survey);
                    Log.e(TAG, "Size of jsonArraySurvey:"+jsonArraySurvey.length());
                }

                }else{
                   Log.e(TAG, "JSON result returning false @_@");
                    return false;
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }


            return true;
        }

        @Override
        protected void onPostExecute(Boolean returResult) {
            super.onPostExecute(returResult);

            if(returResult){
                Log.e(TAG, "Create new custom adapter");
                adapter = new SurveyListAdapter(getApplicationContext(), surveys);

                Log.e(TAG, "Setting adapter");
                mSurveyListView.setAdapter(adapter);
            }else {
                mSurveyListView.setVisibility(View.GONE);
                mNoSurveyTextView.setVisibility(View.VISIBLE);
            }

        }
    }

    class CreateUserSession extends AsyncTask<UserSessions, Void, String>{


        @Override
        protected String doInBackground(UserSessions... userSessionses) {

            HttpHandler handler = new HttpHandler();
            String url = getString(R.string.base_url)+"/createUserSession.php";
            Log.e(TAG, "User Session Data: "+userSessionses[0].getSurveyId());

            JSONObject userSessionJsonObject = new JSONObject();
            try {
                userSessionJsonObject.put("email", userSessionses[0].getEmail());
                userSessionJsonObject.put("surveyId", userSessionses[0].getSurveyId());
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return handler.postServiceCall(url, userSessionJsonObject);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e(TAG, "Create Session Post Execute: "+s.toString());
            Toast.makeText(getApplicationContext(),s.toString(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
            intent.putExtra("surveyId",survey.getId());
            startActivity(intent);
        }
    }


}
