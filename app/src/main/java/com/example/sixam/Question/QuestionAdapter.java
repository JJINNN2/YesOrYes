package com.example.sixam.Question;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sixam.AnswerData;
import com.example.sixam.PercentResponse;
import com.example.sixam.R;
import com.example.sixam.network.RetrofitClient;
import com.example.sixam.network.ServiceApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>{

    int uid;
    String TAG = "YOY QuestionAdapter";
    ArrayList<QuestionItem> items;

    public QuestionAdapter(int uid, ArrayList<QuestionItem> items) {
        this.items = new ArrayList<>();
        this.items = items;
        this.uid = uid;
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder {
        protected TextView writer;
        protected TextView category;
        protected TextView title;
        protected Button button1;
        protected Button button2;
        protected LinearLayout percentLayout;
        protected TextView percentA;
        protected TextView percentB;
        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            writer = itemView.findViewById(R.id.writer);
            category = itemView.findViewById(R.id.category);
            title = itemView.findViewById(R.id.title);
            button1 = itemView.findViewById(R.id.button1);
            button2 = itemView.findViewById(R.id.button2);
            percentLayout = itemView.findViewById(R.id.percentLayout);
            percentA = itemView.findViewById(R.id.percentA);
            percentB = itemView.findViewById(R.id.percentB);

            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    int curAnswer = ((items.get(position).getAnswer()==null)? -1 : items.get(position).getAnswer());
                    int changedAnswer;

                    // 선택이 되어 있지 않았었던 경우
                    if(curAnswer != 0) {
                        changedAnswer = 0;
                        button1.setBackgroundResource(R.drawable.button_round_chose);
                        button2.setBackgroundResource(R.drawable.button_round);
                        percentLayout.setVisibility(View.VISIBLE);
                        setPercentRefresh(percentA, percentB, items.get(position).getQid());
                    }
                    // 선택이 되어있던 경우
                    else {
                        changedAnswer = -1;
                        button1.setBackgroundResource(R.drawable.button_round);
                        percentLayout.setVisibility(View.INVISIBLE);

                    }

                    items.get(position).setAnswer(changedAnswer);
                    setAnswer(new AnswerData(uid, items.get(position).getQid(), changedAnswer));
                }
            });

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    int qid = items.get(position).getQid();
                    int curAnswer = ((items.get(position).getAnswer()==null)? -1 : items.get(position).getAnswer());
                    int changedAnswer;

                    // 선택이 되어 있지 않았었던 경우
                    if(curAnswer != 1) {
                        changedAnswer = 1;
                        button2.setBackgroundResource(R.drawable.button_round_chose_red);
                        button1.setBackgroundResource(R.drawable.button_round);
                        percentLayout.setVisibility(View.VISIBLE);
                        setPercentRefresh(percentA, percentB, items.get(position).getQid());
                    }
                    // 선택이 되어있던 경우
                    else {
                        changedAnswer = -1;
                        button2.setBackgroundResource(R.drawable.button_round);
                        percentLayout.setVisibility(View.INVISIBLE);
                    }

                    items.get(position).setAnswer(changedAnswer);
                    setAnswer(new AnswerData(uid, items.get(position).getQid(), changedAnswer));
                }
            });
        }
    }

    @NonNull
    @Override
    public QuestionAdapter.QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item, parent, false);
        QuestionAdapter.QuestionViewHolder questionViewHolder = new QuestionAdapter.QuestionViewHolder(view);
        return questionViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.QuestionViewHolder holder, int position) {

        Log.d(TAG, "onBindViewHolder");

        int qid = items.get(position).getQid();

        holder.writer.setText("#" + qid + " " + items.get(position).getWriter());
        holder.category.setText("#"+items.get(position).getCategory());
        holder.title.setText(items.get(position).getTitle());
        holder.button1.setText(items.get(position).getA());
        holder.button2.setText(items.get(position).getB());

        if(items.get(position).getAnswer() != null) {
            int answer = items.get(position).getAnswer();
            Log.d(TAG, position +"/answer: "+answer);
            if (answer == -1) {
                holder.button1.setBackgroundResource(R.drawable.button_round);
                holder.button2.setBackgroundResource(R.drawable.button_round);
                holder.percentLayout.setVisibility(View.INVISIBLE);
            }
            else if (answer == 0) {
                holder.button1.setBackgroundResource(R.drawable.button_round_chose);
                holder.button2.setBackgroundResource(R.drawable.button_round);
                holder.percentLayout.setVisibility(View.VISIBLE);
                setPercent(holder.percentA, holder.percentB, items.get(position).getQid());
            }
            else if (answer == 1) {
                holder.button1.setBackgroundResource(R.drawable.button_round);
                holder.button2.setBackgroundResource(R.drawable.button_round_chose_red);
                holder.percentLayout.setVisibility(View.VISIBLE);
                setPercent(holder.percentA, holder.percentB, items.get(position).getQid());
            }

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setAnswer(AnswerData item) {
        Log.d(TAG, "setQuestion");

        ServiceApi getResponse = RetrofitClient.getRetrofit().create(ServiceApi.class);
        Call<SetResponse> call = getResponse.setAnswer(item);
        call.enqueue(new Callback<SetResponse>() {

            @Override
            public void onResponse(Call<SetResponse> call, Response<SetResponse> response) {
                Log.d(TAG, "setAnswer/onResponse");

                if(response.body().getCode() == 200) Log.d(TAG, "no problem");
                else Log.d(TAG, "error");
            }

            @Override
            public void onFailure(Call<SetResponse> call, Throwable t) {
                Log.d(TAG, "setAnswer/onFailure");
            }
        });
    }

    private void setPercent(final TextView viewA, final TextView viewB, int qid) {

        Log.d(TAG, "setPercent");
        ServiceApi getResponse = RetrofitClient.getRetrofit().create(ServiceApi.class);
        Call<PercentResponse> call = getResponse.setPercent(qid);
        call.enqueue(new Callback<PercentResponse>() {

            @Override
            public void onResponse(Call<PercentResponse> call, Response<PercentResponse> response) {
                Log.d(TAG, "setPercent/onResponse");
                viewA.setText(response.body().getPercentA() + "명");
                viewB.setText(response.body().getPercentB() + "명");
                //notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<PercentResponse> call, Throwable t) {
                Log.d(TAG, "setPercent/onFailure");
            }
        });
    }

    private void setPercentRefresh(final TextView viewA, final TextView viewB, int qid) {

        Log.d(TAG, "setPercent");
        ServiceApi getResponse = RetrofitClient.getRetrofit().create(ServiceApi.class);
        Call<PercentResponse> call = getResponse.setPercent(qid);
        call.enqueue(new Callback<PercentResponse>() {

            @Override
            public void onResponse(Call<PercentResponse> call, Response<PercentResponse> response) {
                Log.d(TAG, "setPercent/onResponse");
                viewA.setText(response.body().getPercentA() + "명");
                viewB.setText(response.body().getPercentB() + "명");
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<PercentResponse> call, Throwable t) {
                Log.d(TAG, "setPercent/onFailure");
            }
        });
    }
}
