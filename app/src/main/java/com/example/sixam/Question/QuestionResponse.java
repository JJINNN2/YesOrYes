package com.example.sixam.Question;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class QuestionResponse {

    @SerializedName("qid")
    private ArrayList<Integer> qidList;

    @SerializedName("writer")
    private ArrayList<String> writerList;

    @SerializedName("title")
    private ArrayList<String> titleList;

    @SerializedName("category")
    private ArrayList<String> categoryList;

    @SerializedName("A")
    private ArrayList<String> AList;

    @SerializedName("B")
    private ArrayList<String> BList;

    @SerializedName("answer")
    private ArrayList<Integer> answerList;

    public ArrayList<Integer> getQidList() {
        return qidList;
    }

    public ArrayList<String> getWriterList() {
        return writerList;
    }

    public ArrayList<String> getTitleList() {
        return titleList;
    }

    public ArrayList<String> getCategoryList() {
        return categoryList;
    }

    public ArrayList<String> getAList() {
        return AList;
    }

    public ArrayList<String> getBList() {
        return BList;
    }

    public ArrayList<Integer> getAnswerList() {
        return answerList;
    }
}
