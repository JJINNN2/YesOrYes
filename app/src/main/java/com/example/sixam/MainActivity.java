package com.example.sixam;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.sixam.Question.QuestionAdapter;
import com.example.sixam.Question.QuestionItem;
import com.example.sixam.Question.QuestionResponse;
import com.example.sixam.network.RetrofitClient;
import com.example.sixam.network.ServiceApi;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    String TAG = "YOY MainActivity";
    String userName;
    int uid;

    CollapsingToolbarLayout collapseToolbar;
    ImageView profile;
    RecyclerView recyclerView;
    ImageButton addButton;

    RecyclerView.LayoutManager layoutManager;
    QuestionAdapter questionAdapter;
    ArrayList<QuestionItem> items;

    Animation rotate;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        uid = intent.getIntExtra("uid", -1);

        Log.d(TAG, "onCreate");

        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        collapseToolbar = findViewById(R.id.collpaseToolbar);
        profile = findViewById(R.id.profile);
        recyclerView = findViewById(R.id.recyclerView);
        addButton = findViewById(R.id.addButton);

        collapseToolbar.setTitle("Yes or Yes");

        // 원형으로 자르기
        profile.setBackground(new ShapeDrawable(new OvalShape()));
        profile.setClipToOutline(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        items = new ArrayList<>();
        get50Questions();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MakeQuestionActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");

        addButton.startAnimation(rotate);

        Intent intent = getIntent();
        Log.d(TAG, String.valueOf(intent.getBooleanExtra("add", false)));

        if(intent.getBooleanExtra("add", false)) {
            intent.removeExtra("add");
            items = new ArrayList<>();
            get50Questions();
            intent = new Intent();
        }

        questionAdapter = new QuestionAdapter(uid, items);
        recyclerView.setAdapter(questionAdapter);

    }

    public void get50Questions() {
        Log.d(TAG, "get50Questions");

        ServiceApi getResponse = RetrofitClient.getRetrofit().create(ServiceApi.class);
        //Call<QuestionResponse> call = getResponse.getQuestion(uid);
        Call<QuestionResponse> call = getResponse.getQuestion(uid);
        call.enqueue(new Callback<QuestionResponse>() {

            @Override
            public void onResponse(Call<QuestionResponse> call, Response<QuestionResponse> response) {
                Log.d(TAG, "get50Questions/onResponse");
                ArrayList<Integer> qidList = response.body().getQidList();

                Log.d(TAG, ""+qidList.size());

                ArrayList<String> writerList = response.body().getWriterList();
                ArrayList<String> titleList = response.body().getTitleList();
                ArrayList<String> categoryList = response.body().getCategoryList();
                ArrayList<String> AList = response.body().getAList();
                ArrayList<String> BList = response.body().getBList();
                ArrayList<Integer> answerList =  response.body().getAnswerList();

                for(int i=0; i<qidList.size(); i++) {
                    items.add(new QuestionItem(qidList.get(i), writerList.get(i), titleList.get(i), categoryList.get(i), AList.get(i), BList.get(i), answerList.get(i)));
                    //Log.d(TAG, qidList.get(i)+ writerList.get(i)+ titleList.get(i)+ categoryList.get(i)+ AList.get(i)+BList.get(i)+ answerList.get(i));
                }
                onResume();
            }

            @Override
            public void onFailure(Call<QuestionResponse> call, Throwable t) {
                Log.e(TAG, "get50Questions/onFailure... " + t);

            }
        });

    }
}