package com.example.chanw.surveypsm.Model;

import java.util.Date;

/**
 * Created by chanw on 3/26/2017.
 */

public class Survey {

    private int id;
    private String surveyName;
    private String description;
    private int ownedBy;
    private Date expiryDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(int ownedBy) {
        this.ownedBy = ownedBy;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
