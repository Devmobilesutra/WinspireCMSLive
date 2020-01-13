package com.cms.callmanager.multispinner.clousermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhavesh Chaudhari on 03-Jan-20.
 */
public class QuestionnaireAnswer {
    @SerializedName("Ques_Id")
    @Expose
    int Ques_Id;

    public QuestionnaireAnswer(int ques_Id, String question, String answer, int answer_Id) {
        Ques_Id = ques_Id;
        Question = question;
        Answer = answer;
        Answer_Id = answer_Id;
    }

    @SerializedName("Question")
    @Expose
    String Question;

    @SerializedName("Answer")
    @Expose
    String Answer;

    @SerializedName("Answer_Id")
    @Expose
    int Answer_Id;

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

    public int getAnswer_Id() {
        return Answer_Id;
    }

    public void setAnswer_Id(int answer_Id) {
        Answer_Id = answer_Id;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

}
