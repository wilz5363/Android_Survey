package com.example.chanw.surveypsm.Model;

import java.math.BigInteger;

/**
 * Created by chanw on 4/30/2017.
 */

public class QuestionUserAnswered {
    private String Email;
    private int QuestionId;
    private String ChoiceSelection;
    private String ChoiceSelectionHint;
    private boolean LastQuestion;
    private int SurveyId;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getQuestionId() {
        return QuestionId;
    }

    public void setQuestionId(int questionId) {
        QuestionId = questionId;
    }

    public String getChoiceSelection() {
        return ChoiceSelection;
    }

    public void setChoiceSelection(String choiceSelection) {
        ChoiceSelection = choiceSelection;
    }

    public String getChoiceSelectionHint() {
        return ChoiceSelectionHint;
    }

    public void setChoiceSelectionHint(String choiceSelectionHint) {
        ChoiceSelectionHint = choiceSelectionHint;
    }

    public boolean isLastQuestion() {
        return LastQuestion;
    }

    public void setLastQuestion(boolean lastQuestion) {
        LastQuestion = lastQuestion;
    }

    public int getSurveyId() {
        return SurveyId;
    }

    public void setSurveyId(int surveyId) {
        SurveyId = surveyId;
    }
}
