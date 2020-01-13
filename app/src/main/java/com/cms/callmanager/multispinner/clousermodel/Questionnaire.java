package com.cms.callmanager.multispinner.clousermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhavesh Chaudhari on 03-Jan-20.
 */
public class Questionnaire {
    @SerializedName("Ques_Id")
    @Expose
    int Ques_Id;

    @SerializedName("Question")
    @Expose
    String Question;

    int ansID;
    String ans;

    public int getAnsID() {
        return ansID;
    }

    public void setAnsID(int ansID) {
        this.ansID = ansID;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public String getCommentd() {
        return commentd;
    }

    public void setCommentd(String commentd) {
        this.commentd = commentd;
    }

    String commentd;

    public int getques_Id() {
        return Ques_Id;
    }

    public void setques_Id(int ques_Id) {
        Ques_Id = ques_Id;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }
}
