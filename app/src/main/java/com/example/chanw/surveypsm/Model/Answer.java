package com.example.chanw.surveypsm.Model;

/**
 * Created by chanw on 3/28/2017.
 */

public class Answer {

    private int id;
    private String answer;
    private int selectionCount;
    private int questionId;

    public Answer(int id, String answer) {
        this.id = id;
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getSelectionCount() {
        return selectionCount;
    }

    public void setSelectionCount(int selectionCount) {
        this.selectionCount = selectionCount;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }
}
