package com.example.internet.request;

import com.example.internet.fragment.home.TimelineFragment;
import com.example.internet.util.Global;

import okhttp3.Callback;

public class GetMomentRequest extends BaseRequest{

    String getNewestUrl = Global.API_URL + "/moment/get_new_moment/";

    String getHottestUrl = Global.API_URL + "/moment/get_hot_moment/";

    String getStarredUrl = Global.API_URL + "/moment/get_followings_moment/";

    public GetMomentRequest(Callback saveCallback, int pageAttr, String jwt){
        super();
        try{
            if (pageAttr == TimelineFragment.NEWEST_PAGE){
                getNewestUrl += "0" + "/";
                BaseRequest saveRequest = new BaseRequest();
                saveRequest.get(getNewestUrl, saveCallback, jwt);
            }
            else if (pageAttr == TimelineFragment.HOTTEST_PAGE){
                getHottestUrl += "0" + "/";
                BaseRequest saveRequest = new BaseRequest();
                saveRequest.get(getHottestUrl, saveCallback, jwt);
            }
            else if (pageAttr == TimelineFragment.FOLLOWINGS_PAGE){
                getStarredUrl += "time/0" + "/";
                BaseRequest saveRequest = new BaseRequest();
                saveRequest.get(getStarredUrl, saveCallback, jwt);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
