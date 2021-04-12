package com.example.sixam;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sixam.login.JoinActivity;
import com.example.sixam.login.JoinData;
import com.example.sixam.login.JoinResponse;
import com.example.sixam.network.RetrofitClient;
import com.example.sixam.network.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MakeQuestionActivity extends AppCompatActivity {

    int uid;

    EditText titleEt;
    EditText categoryEt;
    EditText buttonA;
    EditText buttonB;
    ImageButton backButton;
    ConstraintLayout background;

    InputMethodManager imm;

    Button saveBtn;
    Intent intent;

    private ServiceApi service;
    private ProgressBar mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_question);


        intent = getIntent();
        uid = intent.getIntExtra("uid", -1);

        backButton = findViewById(R.id.backButton);
        Animation leftRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.leftright);
        backButton.startAnimation(leftRight);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 배경 터치하면 키보드 숨기기 & 입력란 포커스 지우기
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        background = findViewById(R.id.background);
        background.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                titleEt.clearFocus();
                categoryEt.clearFocus();
                buttonA.clearFocus();
                buttonB.clearFocus();
            }
        });

        titleEt = (EditText) findViewById(R.id.title);
        categoryEt = (EditText) findViewById(R.id.category);
        buttonA = (EditText) findViewById(R.id.btnA);
        buttonB = (EditText) findViewById(R.id.btnB);
        mProgressView = findViewById(R.id.progress_bar);

        saveBtn = (Button) findViewById(R.id.save);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleEt.getText().toString();
                String category = categoryEt.getText().toString();
                String questionA = buttonA.getText().toString();
                String questionB = buttonB.getText().toString();

                if(title.isEmpty())
                    Toast.makeText(getApplicationContext(), "질문을 적어주세요!", Toast.LENGTH_SHORT).show();
                else if(questionA.isEmpty() || questionB.isEmpty())
                    Toast.makeText(getApplicationContext(), "답변을 모두 적어주세요!", Toast.LENGTH_SHORT).show();
                else {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    showProgress(true);
                    if(category.isEmpty())
                        category = "etc";
                    Log.d("Empty Test", "다 채워짐");
                    startMaking(new MakeData(uid, title, category, questionA, questionB));
                }
            }
        });
    }

    private void startMaking(MakeData data) {
        service = RetrofitClient.getRetrofit().create(ServiceApi.class);
        service.makeQuestion(data).enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                UploadResponse result = response.body();
                Toast.makeText(MakeQuestionActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                showProgress(false);

                if (result.getCode() == 200) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("add", true);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                Toast.makeText(MakeQuestionActivity.this, "회원가입 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("회원가입 에러 발생", t.getMessage());
                showProgress(false);
            }
        });
    }
    private void showProgress(boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}