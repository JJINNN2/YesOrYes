package com.example.sixam;

import com.google.gson.annotations.SerializedName;

public class PercentResponse {
    @SerializedName("percentA")
    private int percentA;

    @SerializedName("percentB")
    private int percentB;

    public int getPercentA() {
        return percentA;
    }

    public int getPercentB() {
        return percentB;
    }
}
