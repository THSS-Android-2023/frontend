package com.example.internet.model;

public class SearchResultModel{
    public String content;
    public String timestamp;
    public int id;

    public SearchResultModel(int id, String content, String timestamp){
        this.content = content;
        this.timestamp = timestamp;
        this.id = id;
    }
}
