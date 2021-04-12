package com.example.sixam;

import com.google.gson.annotations.SerializedName;

public class AnswerData {
    @SerializedName("uid")
    private int uid;    // 질문 생성 순서
    @SerializedName("qid")
    private int qid;
    @SerializedName("answer")
    private int answer;

    public AnswerData(int uid, int qid, int answer) {
        this.uid = uid;
        this.qid = qid;
        this.answer = answer;
    }

    public int getUid() {
        return uid;
    }

    public int getQid() {
        return qid;
    }

    public int getAnswer() {
        return answer;
    }
}
