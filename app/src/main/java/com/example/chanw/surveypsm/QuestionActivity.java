package com.example.chanw.surveypsm;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.chanw.surveypsm.Model.Answer;
import com.example.chanw.surveypsm.Model.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

public class QuestionActivity extends AppCompatActivity {

    private static final String TAG = QuestionActivity.class.getSimpleName();
    int mSelectedSurveyId, mIndex;

    Button mNextQuestionBtn, mSubmitBtn, mFchoiceBtn, mSchoiceBtn;
    TextView mQuestionTextView, mPlainTextResult;


    Question mQuestion;
    Answer mAnswer;
    ArrayList<Answer> answerList = new ArrayList<>();

    private int buttonMemory = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        mSelectedSurveyId = getIntent().getExtras().getInt("surveyId");
        mIndex = getIntent().getIntExtra("mIndex", 0);
        Log.e(TAG, "Selected Survey ID:" + mSelectedSurveyId);
        Log.e(TAG, "Index:" + mIndex);


        new GetQuestions().execute();

        mPlainTextResult = (TextView) findViewById(R.id.tv_plain_text_result);
        mQuestionTextView = (TextView) findViewById(R.id.tv_question);

        mFchoiceBtn = (Button) findViewById(R.id.btn_first_choice);
        mFchoiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlainTextResult.setText("10");
                buttonMemory = 1;
            }
        });

        mSchoiceBtn = (Button) findViewById(R.id.btn_second_choice);
        mSchoiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlainTextResult.setText("01");
                buttonMemory = 2;
            }
        });

        mNextQuestionBtn = (Button) findViewById(R.id.btn_next_question);
        mNextQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonMemory != 0) {

                } else {
                    Snackbar.make(view, "Please choose an answer before continue to the next question.", Snackbar.LENGTH_LONG).setAction("No action", null).show();
                }
            }
        });

        mSubmitBtn = (Button) findViewById(R.id.btn_submit);
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                finish();

                if (buttonMemory == 0) {
                    Snackbar.make(view, "Please choose an answer before continue to the next question.", Snackbar.LENGTH_LONG).setAction("No action", null).show();

                } else {
                    Snackbar.make(view, "Thanks for the response and have a nice day!", Snackbar.LENGTH_LONG).setAction("No action", null).show();

                }

            }
        });


    }

    private void NextQuestion(){
        finish();
        Intent intent = getIntent();
        intent.putExtra("surveyId", mSelectedSurveyId);
        intent.putExtra("mIndex", ++mIndex);
        startActivity(intent);
    }

    class GetQuestions extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            HttpHandler httpHandler = new HttpHandler();
            String url = getString(R.string.base_url) + "getQuestions.php?surveyId=" + mSelectedSurveyId + "&index=" + mIndex;
            String jsonResultString = httpHandler.makeServiceCall(url);

            Log.e(TAG, "Response from getQustions.php: " + jsonResultString);

            try {
                JSONObject jsonObject = new JSONObject(jsonResultString);
                if (jsonObject.getBoolean("result")) {
                    JSONArray questionTag = jsonObject.getJSONArray("question");
                    JSONObject questionObject = questionTag.getJSONObject(0);
                    mQuestion = new Question(questionObject.getInt("ID"), questionObject.getString("Question"));

                    JSONArray answerTag = jsonObject.getJSONArray("answers");
                    for (int ansCount = 0; ansCount < answerTag.length(); ansCount++) {
                        JSONObject answerObject = answerTag.getJSONObject(ansCount);
                        mAnswer = new Answer(answerObject.getInt("ID"), answerObject.getString("Answer"));
                        Log.e(TAG, mAnswer.getId() + " " + mAnswer.getAnswer());
                        answerList.add(mAnswer);
                    }
                    if (jsonObject.getBoolean("lastQuestion")) {
                        return true;
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean lastQuestion) {
            super.onPostExecute(lastQuestion);

            mQuestionTextView.setText(mQuestion.getQuestion().toString());
            mFchoiceBtn.setText(answerList.get(0).getAnswer().toString());
            mSchoiceBtn.setText(answerList.get(1).getAnswer().toString());

            if (lastQuestion) {
                mSubmitBtn.setVisibility(View.VISIBLE);
                mNextQuestionBtn.setVisibility(View.GONE);
            }

        }
    }

    class PostAnswer extends AsyncTask<String, Void, Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            HttpHandler handler = new HttpHandler();
            String url = getString(R.string.base_url)+"postAnswer.php";

            JSONObject postObject = new JSONObject();


            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }


}
