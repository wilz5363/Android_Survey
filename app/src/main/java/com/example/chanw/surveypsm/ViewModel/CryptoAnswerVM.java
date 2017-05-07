package com.example.chanw.surveypsm.ViewModel;

/**
 * Created by chanw on 5/5/2017.
 */

public class CryptoAnswerVM {

    private int answerId;
    private String cipherText;
    private String key;
    private int questionId;
    private String email;
    private boolean lastQuestion;

    public CryptoAnswerVM(int answerId, String cipherText, String key, int questionId, String email, boolean lastQuestion) {
        this.answerId = answerId;
        this.cipherText = cipherText;
        this.key = key;
        this.questionId = questionId;
        this.email = email;
        this.lastQuestion = lastQuestion;
    }

    public int getAnswerId() {
        return answerId;
    }

    public String getCipherText() {
        return cipherText;
    }

    public String getKey() {
        return key;
    }

    public int getQuestionId() {
        return questionId;
    }

    public String getEmail() {
        return email;
    }

    public boolean isLastQuestion() {
        return lastQuestion;
    }
}
