package com.example.internet.request.message;

import com.example.internet.request.BaseRequest;
import com.example.internet.util.Global;

import okhttp3.Callback;

public class GetMessageRequest extends BaseRequest {
    private String url = Global.API_URL + "/chat/get_message/";

    public GetMessageRequest(Callback callback, String target, String jwt) {
        super();
        url += target + "/";
        get(url, callback, jwt);
    }

    public GetMessageRequest(Callback callback, String target, String jwt, int lastMessageId, String direction) {
        super();
        url += target + "/" + lastMessageId + "/" + direction + "/";
        get(url, callback, jwt);
    }
}
