package com.cms.callmanager.multispinner;

import com.cms.callmanager.multispinner.clousermodel.ActivityType;
import com.cms.callmanager.multispinner.clousermodel.ErrorCode;
import com.cms.callmanager.multispinner.clousermodel.GetFuturePartReplace;
import com.cms.callmanager.multispinner.clousermodel.GetIdleReason;
import com.cms.callmanager.multispinner.clousermodel.GetSubModuleAffected;
import com.cms.callmanager.multispinner.clousermodel.ModuleAffected;
import com.cms.callmanager.multispinner.clousermodel.ProblemFix;
import com.cms.callmanager.multispinner.clousermodel.Questionnaire;
import com.cms.callmanager.multispinner.clousermodel.QuestionnaireAnswer;
import com.cms.callmanager.multispinner.clousermodel.ResponseCategory;
import com.cms.callmanager.multispinner.clousermodel.Solution;
import com.google.gson.JsonObject;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Bhavesh Chaudhari on 31-Dec-19.
 */
public interface IRetrofit {
    @GET("ProblemFix")
    Call<List<ProblemFix>> getProblemFix();

    @GET("Solution")
    Call<List<Solution>> getSolution();

    @GET("ResponseCategory")
    Call<List<ResponseCategory>> getResponseCategory();

    @GET("ActivityType")
    Call<List<ActivityType>> getActivityType();

    @GET("Questionnaire")
    Call<List<Questionnaire>> getQuestionnaire();

    @GET("QuestionnaireAnswer")
    Call<List<QuestionnaireAnswer>> getQuestionnaireAnswer();

    @GET("ErrorCode")
    Call<List<ErrorCode>> getErrorCode();

    @GET("ModuleAffected")
    Call<List<ModuleAffected>> getModuleAffected();

    @GET("GetFuturePartReplace")
    Call<List<GetFuturePartReplace>> getGetFuturePartReplace();

    @GET("GetSubModuleAffected")
    Call<List<GetSubModuleAffected>> getGetSubModuleAffected(@Query("ModuleId") String moduleId);

    @GET("GetIdleReason")
    Call<List<GetIdleReason>> getGetIdleReason();

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("SaveAllCallClosure")
    Call<JsonObject> SendSpinnerpart(@Query("DocketNo") String DocketNo, @Body JSONObject data);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("InsertQuestionnaire")
    Call<JsonObject> SendQuestionpart(@Query("DocketNo") String DocketNo, @Body JSONArray data);


    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("AddTicketAttachmentFiles?UserId=123465")
    Call<JsonObject> SendImagespart(@Body String data);

}
