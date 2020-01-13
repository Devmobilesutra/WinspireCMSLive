package com.cms.callmanager.multispinner.clousermodel;

/**
 * Created by Bhavesh Chaudhari on 08-Jan-20.
 */
public class SelectedQuesModel {
    int Ques_Id;



    String Question;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    String comment;

    public int getQues_Id() {
        return Ques_Id;
    }

    public void setQues_Id(int ques_Id) {
        Ques_Id = ques_Id;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public int getAnswer_Id() {
        return Answer_Id;
    }

    public void setAnswer_Id(int answer_Id) {
        Answer_Id = answer_Id;
    }

    String Answer;


    int Answer_Id;

    public SelectedQuesModel(int ques_Id, String question, String answer, int answer_Id) {
        Ques_Id = ques_Id;
        Question = question;
        Answer = answer;
        Answer_Id = answer_Id;
    }



}
