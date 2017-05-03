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
import android.widget.Toast;

import com.example.chanw.surveypsm.Model.Answer;
import com.example.chanw.surveypsm.Model.Question;
import com.example.chanw.surveypsm.Model.QuestionUserAnswered;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {

    private static final String TAG = QuestionActivity.class.getSimpleName();
    private String key;
    private Boolean mLastQuestion;
    int mSelectedSurveyId, mIndex;

    Button mNextQuestionBtn, mSubmitBtn, mFchoiceBtn, mSchoiceBtn, mEncryptBtn;
    TextView mQuestionTextView, mPlainTextResult, mEncryptedTextResult;


    Question mQuestion;
    Answer mAnswer;
    ArrayList<Answer> answerList = new ArrayList<>();

    private int buttonMemory = 0;
    SessionManager sessionManager;
    static HttpHandler handler = new HttpHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();
//        new CreateUserSurveySession().execute();


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
                buttonMemory = (int) mFchoiceBtn.getTag();
            }
        });

        mSchoiceBtn = (Button) findViewById(R.id.btn_second_choice);
        mSchoiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlainTextResult.setText("01");
                buttonMemory = (int) mSchoiceBtn.getTag();
            }
        });

        mNextQuestionBtn = (Button) findViewById(R.id.btn_next_question);
        mNextQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonMemory != 0) {
//                    new PostAnswer().execute(buttonMemory);
                    QuestionUserAnswered answered = new QuestionUserAnswered();
                    answered.setEmail(sessionManager.getUserEmail());
                    answered.setQuestionId(mQuestion.getId());
                    answered.setChoiceSelection(mEncryptedTextResult.getText().toString());
                    answered.setChoiceSelectionHint(key);
                    answered.setLastQuestion(false);
                    answered.setSurveyId(mSelectedSurveyId);
                    mLastQuestion = false;
                    new PostAnswer().execute(answered);

                } else {
                    Snackbar.make(view, "Please choose an answer before continue to the next question.", Snackbar.LENGTH_LONG).setAction("No action", null).show();
                }
            }
        });

        mSubmitBtn = (Button) findViewById(R.id.btn_submit);
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (buttonMemory == 0) {
                    Snackbar.make(view, "Please choose an answer before continue to the next question.", Snackbar.LENGTH_LONG).setAction("No action", null).show();

                } else {
                    QuestionUserAnswered answered = new QuestionUserAnswered();
                    answered.setEmail(sessionManager.getUserEmail());
                    answered.setQuestionId(mQuestion.getId());
                    answered.setChoiceSelection(mEncryptedTextResult.getText().toString());
                    answered.setChoiceSelectionHint(key);
                    answered.setLastQuestion(true);
                    answered.setSurveyId(mSelectedSurveyId);

                    Log.e(TAG, "When User Submit last question: "+answered.isLastQuestion());
                    mLastQuestion = true;
                    new PostAnswer().execute(answered);
                    Snackbar.make(view, "Thanks for the response and have a nice day!", Snackbar.LENGTH_LONG).setAction("No action", null).show();
                    finish();
                }

            }
        });

        mEncryptedTextResult = (TextView) findViewById(R.id.tv_encrypt_text_result);

        mEncryptBtn = (Button) findViewById(R.id.btn_encrypt);
        mEncryptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonMemory == 0){
                    Snackbar.make(view, "Please choose an answer before continue to encrypt the plaintext", Snackbar.LENGTH_SHORT).setAction("No action", null).show();
                }else{
                    String values[] = new String[3];
                    values[0] = sessionManager.getUserEmail();
                    values[1] = String.valueOf(mSelectedSurveyId);
                    values[2] = String.valueOf(buttonMemory);

                    new EncryptPlainText().execute(values);
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



    //AsyncTask

    class GetQuestions extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            String url = getString(R.string.base_url) + "getQuestions.php?surveyId=" + mSelectedSurveyId + "&index=" + mIndex;
            String jsonResultString = handler.makeServiceCall(url);

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
            mFchoiceBtn.setTag(answerList.get(0).getId());

            mSchoiceBtn.setText(answerList.get(1).getAnswer().toString());
            mSchoiceBtn.setTag(answerList.get(1).getId());

            if (lastQuestion) {
                mSubmitBtn.setVisibility(View.VISIBLE);
                mNextQuestionBtn.setVisibility(View.GONE);
                mLastQuestion = true;
            }

        }
    }

    class EncryptPlainText extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            HttpHandler handler = new HttpHandler();
            String url = getString(R.string.base_url) + "encryptValue.php";
            Log.e(TAG, "Url for Encrypt PlainText: "+url);
            JSONObject params = new JSONObject();
            try{
                params.put("surveyId", strings[1]);
                params.put("email", strings[0]);
                params.put("selectionValue", strings[2]);

                Log.e(TAG,"Create new JSONObject: "+strings[2]);
            }catch (JSONException e){
                Log.e(TAG, "Ecrypt PlainText: "+e.getMessage());
            }

            return handler.postServiceCallWithReturnJson(url, params);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e(TAG,"OnPostExecute result: " +result);
            try {
                JSONObject jsonResult = new JSONObject(result);
                //JSONArray cipherTextArray =  jsonResult.getJSONArray("cipherText");

                //Log.e(TAG,"Encrypt JSON: "+jsonResult.getString("value"));

//                JSONObject cipherText = cipherTextArray.getJSONObject(0);
                mEncryptedTextResult.setText(jsonResult.getString("cipherText"));
                key = jsonResult.getString("key");
            } catch (JSONException e) {
               Log.e(TAG,"EncryptPlainText: "+e.getMessage());

            }

        }
    }

    class PostAnswer extends AsyncTask<QuestionUserAnswered, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(QuestionUserAnswered... answereds) {

            String url = getString(R.string.base_url)+"postAnswer.php";
            JSONObject params = new JSONObject();
            try{

                params.put("questionId", answereds[0].getQuestionId());
                params.put("cipherText", answereds[0].getChoiceSelection());
                params.put("email", answereds[0].getEmail());
                params.put("key", answereds[0].getChoiceSelectionHint());
                params.put("lastQuestion", answereds[0].isLastQuestion());
                params.put("surveyId", answereds[0].getSurveyId());


            }catch (JSONException je){
                Log.e(TAG, "JSON Exception"+je.getMessage());
            }


            return handler.postServiceCall(url, params);
    }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            Log.e(TAG,"Submit answers on PostAnswer: "+result.toString());
            if(mLastQuestion){
                finish();
            }else{
                NextQuestion();
            }
        }
    }


}
