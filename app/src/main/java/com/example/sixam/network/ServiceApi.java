package com.example.sixam.network;

import com.example.sixam.AnswerData;
import com.example.sixam.MakeData;
import com.example.sixam.PercentResponse;
import com.example.sixam.Question.QuestionItem;
import com.example.sixam.Question.QuestionResponse;
import com.example.sixam.Question.SetResponse;
import com.example.sixam.UploadResponse;
import com.example.sixam.login.JoinData;
import com.example.sixam.login.JoinResponse;
import com.example.sixam.login.LoginData;
import com.example.sixam.login.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServiceApi {

    @POST("/user/login")
    Call<LoginResponse> userLogin(@Body LoginData data);

    @POST("/user/join")
    Call<JoinResponse> userJoin(@Body JoinData data);

    @POST("/question/upload")
    Call<UploadResponse> makeQuestion(@Body MakeData data);

    @POST("/question/get")
    Call<QuestionResponse> getQuestion(@Query("uid") int uid);

    @POST("/question/set")
    Call<SetResponse> setQuestion(@Body QuestionItem data);

    @POST("/answer/set")
    Call<SetResponse> setAnswer(@Body AnswerData data);

    @POST("/question/percent")
    Call<PercentResponse> setPercent(@Query("qid") int pid);

}
