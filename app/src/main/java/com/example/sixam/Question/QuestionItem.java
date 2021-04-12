package com.example.sixam.Question;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class QuestionItem {
    @SerializedName("qid")
    private int qid;    // 질문 생성 순서
    @SerializedName("writer")
    private String writer;
    @SerializedName("title")
    private String title;
    @SerializedName("category")
    private String category;
    @SerializedName("A")
    private String A;
    @SerializedName("B")
    private String B;
    @SerializedName("answer")
    private Integer answer;

    public QuestionItem(int qid, String writer, String title, String category, String A, String B, Integer answer) {
        this.qid = qid;
        this.writer = writer;
        this.title = title;
        this.category = category;
        this.A = A;
        this.B = B;
        this.answer = answer;
    }

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getA() {
        return A;
    }

    public void setA(String A) {
        this.A = A;
    }

    public String getB() {
        return B;
    }

    public void setB(String B) {
        this.B = B;
    }

    public Integer getAnswer() {
        return answer;
    }

    public void setAnswer(Integer answer) {
        this.answer = answer;
    }
}
