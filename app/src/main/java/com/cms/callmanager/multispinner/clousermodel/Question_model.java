package com.cms.callmanager.multispinner.clousermodel;

/**
 * Created by Bhavesh Chaudhari on 07-Jan-20.
 */
public class Question_model {
    String Question;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    String comment;

    public Question_model(String question, String answer, String quesId, String answerId,String Cocomment) {
        Question = question;
        Answer = answer;
        QuesId = quesId;
        AnswerId = answerId;
        comment = Cocomment;
    }

    String Answer;

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

    public String getQuesId() {
        return QuesId;
    }

    public void setQuesId(String quesId) {
        QuesId = quesId;
    }

    public String getAnswerId() {
        return AnswerId;
    }

    public void setAnswerId(String answerId) {
        AnswerId = answerId;
    }

    String QuesId, AnswerId;

}
