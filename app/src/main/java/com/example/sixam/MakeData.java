package com.example.sixam;

import com.google.gson.annotations.SerializedName;

public class MakeData {

    @SerializedName("uid")
    private int uid;

    @SerializedName("title")
    private String title;

    @SerializedName("category")
    private String category;

    @SerializedName("btnA")
    private String btnA;

    @SerializedName("btnB")
    private String btnB;

    public MakeData(int uid, String title, String category, String btnA, String btnB) {
        this.uid = uid;
        this.title = title;
        this.category = category;
        this.btnA = btnA;
        this.btnB = btnB;
    }
}