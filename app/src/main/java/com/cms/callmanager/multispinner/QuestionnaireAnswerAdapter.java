package com.cms.callmanager.multispinner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import com.cms.callmanager.R;
import com.cms.callmanager.multispinner.clousermodel.Question_model;
import com.cms.callmanager.multispinner.clousermodel.Questionnaire;
import com.cms.callmanager.multispinner.clousermodel.QuestionnaireAnswer;
import com.cms.callmanager.multispinner.clousermodel.SelectedQuesModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bhavesh Chaudhari on 03-Jan-20.
 */
public class QuestionnaireAnswerAdapter extends RecyclerView.Adapter<QuestionnaireAnswerAdapter.QuesViewHolder> {

    private Context mContext;
    private List<Questionnaire> QuestionnaireList;
    private List<QuestionnaireAnswer> QuestionnaireAnswerList;

    private ArrayAdapter<QuestionnaireAnswer> dataAdapter;
    private ArrayAdapter<String> dataAdapter1;
    private ArrayList<String> newquesAnslist1_id;
    private ArrayList<Question_model> fillQuesArray;
  //  ArrayList<SelectedQuesModel> quesSelectedArray = new ArrayList<>();

    public QuestionnaireAnswerAdapter(Context context, List<Questionnaire> questionnaireList, List<QuestionnaireAnswer> questionnaireAnswerList, ArrayList<Question_model> quesData){
        this.mContext = context;
        this.QuestionnaireList = questionnaireList;
        this.QuestionnaireAnswerList=questionnaireAnswerList;
        this.fillQuesArray  = quesData;

    }

    @Override
    public QuesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.questionair_item_layout, parent, false);
        QuesViewHolder userViewHolder = new QuesViewHolder(view);
        return userViewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull QuesViewHolder holder, int position) {


        holder.spinnerAnswer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            private Adapter initializedAdapter;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position1, long id) {
                if( initializedAdapter !=parent.getAdapter() ) {
                    initializedAdapter = parent.getAdapter();
                    return;
                }

                String item = parent.getItemAtPosition(position1).toString();
                QuestionnaireList.get(position).setAns(item);
                Log.d("val : ",item);
                for (int i =0;i < QuestionnaireAnswerList.size();i++)
                {
                    if(item.equalsIgnoreCase(QuestionnaireAnswerList.get(i).getAnswer()))
                    {
                        int id1 = QuestionnaireAnswerList.get(i).getAnswer_Id();

                        holder.spinneranswerid.setText(String.valueOf(QuestionnaireAnswerList.get(i).getAnswer_Id()));
                        QuestionnaireList.get(position).setAnsID(QuestionnaireAnswerList.get(i).getAnswer_Id());
                    }
                }
                // Log.d("spinner val : =",QuestionnaireAnswerList.get(position).getQuestion());
                // Showing selected spinner item
                //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        holder.question.setText(""+QuestionnaireList.get(position).getQuestion());
       // holder.tv_2.setText(userList.get(position).getUserName());

        QuestionnaireAnswer quesAns =null;

        List<QuestionnaireAnswer> newquesAnslist = new ArrayList<>();
        List<String> newquesAnslist1 = new ArrayList<>();
        newquesAnslist1_id = new ArrayList<>();
        newquesAnslist1_id.clear();
        newquesAnslist1.add("--Select--");
        for (int j =0; j < QuestionnaireAnswerList.size();j++ )
        {
            int  a= QuestionnaireList.get(position).getques_Id();
            int b = QuestionnaireAnswerList.get(j).getques_Id();
            if(QuestionnaireList.get(position).getQuestion().equals(QuestionnaireAnswerList.get(j).getQuestion()))
            {
               /* quesAns = new QuestionnaireAnswer(QuestionnaireAnswerList.get(j).getques_Id(),QuestionnaireAnswerList.get(j).getQuestion(),
                        QuestionnaireAnswerList.get(j).getAnswer(),QuestionnaireAnswerList.get(j).getAnswer_Id());
                newquesAnslist.add(quesAns);*/
               newquesAnslist1.add(QuestionnaireAnswerList.get(j).getAnswer());
                newquesAnslist1_id.add(String.valueOf(QuestionnaireAnswerList.get(j).getAnswer_Id()));
            }
        }


       /* dataAdapter = new ArrayAdapter<QuestionnaireAnswer>(mContext,
                android.R.layout.simple_spinner_item, newquesAnslist1);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerAnswer.setAdapter(dataAdapter);*/
        dataAdapter1 = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_item, newquesAnslist1);
        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerAnswer.setAdapter(dataAdapter1);
       /* holder.spinnerAnswer.setSelection(0, true);
        holder.spinnerAnswer.setSelection(newquesAnslist1.size() - 1, true);*/

        if(fillQuesArray != null)
        {

            String batchno = fillQuesArray.get(position).getAnswer();
            System.out.println("batch No : "+batchno);
            if (batchno != null) {
                //  holder.spnBatch.setText(batchno);
                int spinnerPosition = dataAdapter1.getPosition(batchno);
                holder.spinnerAnswer.setSelection(spinnerPosition);
                holder.commentEditext.setText(fillQuesArray.get(position).getComment());
                // holder.spnBatch.setEnabled(false);
            }

        }


     /*   dataAdapter1 = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_item, newquesAnslist1_id);
        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerAnswerid.setAdapter(dataAdapter1);*/

        holder.commentEditext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                QuestionnaireList.get(position).setCommentd(s.toString());
            }
        });

    }




    @Override
    public int getItemCount() {
        return QuestionnaireList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public List<Questionnaire> getData() {
        return QuestionnaireList;
    }

    public  class QuesViewHolder extends RecyclerView.ViewHolder{

        public TextView question;
        public Spinner spinnerAnswer;
        public TextView spinneranswerid;
        public EditText commentEditext;

        public QuesViewHolder(View itemView) {
            super(itemView);
            question = (TextView)itemView.findViewById(R.id.tvQuestions);

            spinnerAnswer = (Spinner)itemView.findViewById(R.id.spinnerAnswerId);
            spinneranswerid = (TextView)itemView.findViewById(R.id.spinnerAnswerId_hidden);
            commentEditext = (EditText)itemView.findViewById(R.id.edtTxtCommevnt);



            spinnerAnswer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // On selecting a spinner item
                    String item = parent.getItemAtPosition(position).toString();

                    Log.d("val : ",item);
                    for (int i =0;i < QuestionnaireAnswerList.size();i++)
                    {
                        if(item.equals(QuestionnaireAnswerList.get(i).getAnswer()))
                        {
                            spinneranswerid.setText(String.valueOf(QuestionnaireAnswerList.get(i).getAnswer_Id()));
                        }
                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull QuesViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if ((getItemCount() - 1) == holder.getAdapterPosition()){
            Log.d("","list done");
           // onLoadMore(current_page);
        } else {
            Log.d("","remain item : " + (getItemCount() - holder.getAdapterPosition() - 1));
        }
    }
}
