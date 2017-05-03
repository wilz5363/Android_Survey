package com.example.chanw.surveypsm.Model;

/**
 * Created by chanw on 4/25/2017.
 */

public class UserSessions {

    private int ID;
    private int UserId;
    private String Email;
    private int SurveyId;
    private long Session_Hash;
    private boolean Completed;


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getSurveyId() {
        return SurveyId;
    }

    public void setSurveyId(int surveyId) {
        SurveyId = surveyId;
    }

    public long getSession_Hash() {
        return Session_Hash;
    }

    public void setSession_Hash(long session_Hash) {
        Session_Hash = session_Hash;
    }

    public boolean isCompleted() {
        return Completed;
    }

    public void setCompleted(boolean completed) {
        Completed = completed;
    }
}
