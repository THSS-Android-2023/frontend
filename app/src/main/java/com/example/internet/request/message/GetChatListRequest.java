package com.example.internet.request.message;

import com.example.internet.request.BaseRequest;
import com.example.internet.util.Global;

import okhttp3.Callback;

public class GetChatListRequest extends BaseRequest {
    private String url = Global.API_URL + "/chat/get_chatter/";

    public GetChatListRequest(Callback callback, String jwt) {
        super();
        get(url, callback, jwt);
    }
}
