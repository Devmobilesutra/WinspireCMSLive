package com.cms.callmanager.multispinner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cms.callmanager.R;
import com.cms.callmanager.multispinner.clousermodel.Question_model;
import com.cms.callmanager.multispinner.clousermodel.Questionnaire;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuestionairActivity extends AppCompatActivity {
    private RecyclerView questionarRecyclerview;
    private Button QuesSubmitBtn,QuesCloseBtn;
   public static JSONArray QuestionJsonArray ;
     QuestionnaireAnswerAdapter QuestionnaireAdapter;
    public static ArrayList<Question_model> data;
    private JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questionnair_dialog_layout);
        // Hide ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        this.setFinishOnTouchOutside(false);

        questionarRecyclerview=(RecyclerView)findViewById(R.id.questionarRecyclerview);
        questionarRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        if(CallClloserActivity.Questionnairelist != null)
        {
             QuestionnaireAdapter = new QuestionnaireAnswerAdapter(getApplicationContext(), CallClloserActivity.Questionnairelist, CallClloserActivity.QuestionnaireAnswerlist,data);
            questionarRecyclerview.setAdapter(QuestionnaireAdapter);
        }


        QuesSubmitBtn = (Button) findViewById(R.id.btnSubmitQues);
        QuesCloseBtn = (Button) findViewById(R.id.btnCloseQues);

       /* QuesSubmitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

               // QuestionnaireAdapter.notifyDataSetChanged();
                TextView QuesTextview = null;
                Spinner spinnervalues = null;
                TextView spinnervalues_ansId = null;
                EditText etComment = null;
                QuestionnaireAnswerAdapter.QuesViewHolder viewHolder = null;
                ArrayList<String> ansArray = new ArrayList<>();
                int val = QuestionnaireAdapter.getItemCount();
                View itemView;

                *//*for (int childCount = questionarRecyclerview.getChildCount(), i = 0; i < childCount; ++i) {
                    viewHolder = (QuestionnaireAnswerAdapter.QuesViewHolder)questionarRecyclerview.getChildViewHolder(questionarRecyclerview.getChildAt(i));
                    itemView = questionarRecyclerview.getLayoutManager().findViewByPosition(i);
                }*//*

                *//*View v1;
                int itemCount = questionarRecyclerview.getChildCount();
                for(int i = 0; i < itemCount; i++){
                    v1 = questionarRecyclerview.getChildAt(i); *//**//* assuming you have your EditText in a view group like LinearLayout*//**//*
                    EditText et = v1.findViewById(R.id.tvQuestions);
                    String text = et.getText().toString();
                    // todo process text
                }*//*

             //  QuestionnaireAnswerAdapter.QuesViewHolder holder = (QuestionnaireAnswerAdapter.QuesViewHolder) questionarRecyclerview.getChildViewHolder(v);

                for (int i = 0; i < QuestionnaireAdapter.getItemCount(); i++) {
                  //  viewHolder =(QuestionnaireAnswerAdapter.QuesViewHolder) questionarRecyclerview.findViewHolderForItemId(i);
                      viewHolder = (QuestionnaireAnswerAdapter.QuesViewHolder)questionarRecyclerview.findViewHolderForAdapterPosition(i);
                 //   viewHolder =   (QuestionnaireAnswerAdapter.QuesViewHolder)  questionarRecyclerview.findViewHolderForItemId(QuestionnaireAdapter.getItemId(i));
                    //  itemView = questionarRecyclerview.findViewHolderForAdapterPosition(i).itemView;
                    //     viewHolder.spinnerAnswerid.clearFocus();
                    spinnervalues_ansId = viewHolder.spinneranswerid;
                    // TextView dis = viewHolder.discription;
                    if(spinnervalues_ansId.getText().toString().length() != 0)
                    {
                        ansArray.add(spinnervalues_ansId.getText().toString());
                    }


                }

                if(ansArray.size() > 0)
                {
                    data=new ArrayList<>();
                    QuestionJsonArray = new JSONArray();
                    for (int i = 0; i < QuestionnaireAdapter.getItemCount(); i++) {
                        viewHolder = (QuestionnaireAnswerAdapter.QuesViewHolder) questionarRecyclerview.findViewHolderForAdapterPosition(i);
                      //  viewHolder =   (QuestionnaireAnswerAdapter.QuesViewHolder)  questionarRecyclerview.findViewHolderForItemId(QuestionnaireAdapter.getItemId(i));
                        QuesTextview = viewHolder.question;
                        spinnervalues = viewHolder.spinnerAnswer;
                        spinnervalues_ansId = viewHolder.spinneranswerid;
                        etComment = viewHolder.commentEditext;
                        // TextView dis = viewHolder.discription;

                        jsonObject = new JSONObject();
                        Question_model ques_mode = null;
                        if (spinnervalues_ansId.getText().toString().length() != 0)
                        {


                            try {
                                jsonObject.put("QuesId", CallClloserActivity.Questionnairelist.get(i).getques_Id());
                                jsonObject.put("Question", QuesTextview.getText().toString());

                                jsonObject.put("AnswerId", spinnervalues_ansId.getText().toString());
                                jsonObject.put("Answer", spinnervalues.getSelectedItem().toString());
                                jsonObject.put("DisplayToCustomer", "1");
                                jsonObject.put("Comments", etComment.getText().toString());
                                jsonObject.put("Last_Modified_By", "");

                                ques_mode = new Question_model(QuesTextview.getText().toString(),spinnervalues.getSelectedItem().toString(),
                                        String.valueOf(CallClloserActivity.Questionnairelist.get(i).getques_Id()),spinnervalues_ansId.getText().toString(),etComment.getText().toString());
                                data.add(ques_mode);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            QuestionJsonArray.put(jsonObject);
                        }else {
                            ques_mode = new Question_model("","--Select--","" ,"","");
                            data.add(ques_mode);
                        }

                    }
                    Log.d("Question array : ",QuestionJsonArray.toString());
                }

                finish();
            }
        });*/

        QuesSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int val = QuestionnaireAdapter.getItemCount();
                    List<Questionnaire> questionnaires = QuestionnaireAdapter.getData();
                    data = new ArrayList<>();
                    QuestionJsonArray = new JSONArray();
                    for (int i = 0; i < questionnaires.size(); i++) {
                        jsonObject = new JSONObject();
                        Question_model ques_mode = null;
                        int iii = questionnaires.get(i).getAnsID();
                      //  String comm = questionnaires.get(i).getCommentd();
                        if (questionnaires.get(i).getAnsID() != 0) {
                            try {
                                jsonObject.put("QuesId", CallClloserActivity.Questionnairelist.get(i).getques_Id());
                                jsonObject.put("Question", questionnaires.get(i).getQuestion());
                                jsonObject.put("AnswerId", questionnaires.get(i).getAnsID());
                                jsonObject.put("Answer", questionnaires.get(i).getAns());
                                jsonObject.put("DisplayToCustomer", "1");
                                if(questionnaires.get(i).getCommentd() == null)
                                {
                                    jsonObject.put("Comments","");
                                }else {
                                    jsonObject.put("Comments", questionnaires.get(i).getCommentd());
                                }

                                jsonObject.put("Last_Modified_By", "");
                                ques_mode = new Question_model(questionnaires.get(i).getQuestion(), questionnaires.get(i).getAns(), String.valueOf(CallClloserActivity.Questionnairelist.get(i).getques_Id()), String.valueOf(questionnaires.get(i).getAnsID()), questionnaires.get(i).getCommentd());
                                data.add(ques_mode);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            QuestionJsonArray.put(jsonObject);
                        } else {
                            ques_mode = new Question_model("", "--Select--", "", "", "");
                            data.add(ques_mode);
                        }
                    }
                    Log.d("Question array : ", QuestionJsonArray.toString());
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        QuesCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
