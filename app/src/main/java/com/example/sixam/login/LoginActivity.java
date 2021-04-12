package com.example.sixam.login;

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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sixam.MainActivity;
import com.example.sixam.R;
import com.example.sixam.network.RetrofitClient;
import com.example.sixam.network.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    String TAG = "YOY LoginActivity";

    ImageView logoImage;

    InputMethodManager imm;
    ConstraintLayout background;
    AutoCompleteTextView mEmailView;
    EditText mPasswordView;
    Button mLoginButton;
    TextView mJoinTv;

    private ProgressBar mProgressView;
    private ServiceApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logoImage = findViewById(R.id.logoImage);
        Animation updown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.updown);
        logoImage.startAnimation(updown);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        background = findViewById(R.id.background);
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        mLoginButton = findViewById(R.id.loginButton);
        mJoinTv = findViewById(R.id.join);
        mProgressView = findViewById(R.id.progress_bar);


        // 배경 터치하면 키보드 숨기기 & 입력란 포커스 지우기
        background.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                mEmailView.clearFocus();
                mPasswordView.clearFocus();
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // attemptLogin
                attemptLogin();
            }
        });

        mJoinTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });
    }


    private void attemptLogin() {
        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean isProblem = false;
        View focusView = null;

        // 패스워드의 유효성 검사
        if (password.isEmpty()) {
            mEmailView.setError("비밀번호를 입력해주세요.");
            focusView = mEmailView;
            isProblem = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError("6자 이상의 비밀번호를 입력해주세요.");
            focusView = mPasswordView;
            isProblem = true;
        }

        // 이메일의 유효성 검사
        if (email.isEmpty()) {
            mEmailView.setError("이메일을 입력해주세요.");
            focusView = mEmailView;
            isProblem = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError("@를 포함한 유효한 이메일을 입력해주세요.");
            focusView = mEmailView;
            isProblem = true;
        }

        if (isProblem) {
            focusView.requestFocus();
            Log.d(TAG, "attemptLogin: there is problem.");
        } else {
            startLogin(new LoginData(email, password));
            showProgress(true);
            Log.d(TAG, "attemptLogin: 입력한 값 형식에는 문제 없음");
        }
    }
    private void startLogin(LoginData data) {
        Log.d(TAG, "startLogin");
        service = RetrofitClient.getRetrofit().create(ServiceApi.class);
        service.userLogin(data).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.body().getCode() == 200) {
                    Log.d(TAG, "startLogin/onResponse: Login complete!");

                    // MainActivity 이동
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                    intent.putExtra("userName", response.body().getUserName());
                    intent.putExtra("uid", response.body().getUid());
                    startActivity(intent);
                    finish();

                } else {
                    // response.body().getCode() == 204
                    Log.d(TAG, "startLogin/onResponse: " + response.body().getMessage());
                }

                Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                showProgress(false);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "로그인 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "startLogin/onFailure: " + t.getMessage());
                showProgress(false);
            }
        });

    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    private void showProgress(boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}