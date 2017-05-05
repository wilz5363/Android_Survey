package com.example.chanw.surveypsm.ViewModel;

/**
 * Created by chanw on 5/5/2017.
 */

public class CryptoAnswerVM {

    private int answerId;
    private String cipherText;
    private String key;

    public CryptoAnswerVM(int answerId, String cipherText, String key) {
        this.answerId = answerId;
        this.cipherText = cipherText;
        this.key = key;
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
}
