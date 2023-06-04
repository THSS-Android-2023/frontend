package com.example.internet.request.message;

import com.example.internet.request.BaseRequest;
import com.example.internet.util.Global;

import okhttp3.Callback;

public class SendMessageRequest extends BaseRequest {
    private String url = Global.API_URL + "/chat/send_message/";

    public SendMessageRequest(Callback callback, String target, String content, String jwt) {
        super();
        addParam("target_user", target);
        addParam("content", content);
        post(url, callback, jwt);
    }
}
