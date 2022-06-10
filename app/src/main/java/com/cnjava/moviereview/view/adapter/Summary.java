package com.cnjava.moviereview.view.adapter;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Summary implements Serializable {
    @SerializedName("summary_text")
    public String summaryText;
}
