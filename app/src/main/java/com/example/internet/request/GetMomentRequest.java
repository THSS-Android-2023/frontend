package com.example.internet.request;

import com.example.internet.fragment.TimelineFragment;
import com.example.internet.util.Global;

import okhttp3.Callback;

public class GetMomentRequest extends BaseRequest{

    String getNewestUrl = Global.API_URL + "/moment/get_new_moment/";

    String getHottestUrl = Global.API_URL + "/moment/get_hot_moment/";

    String getFollowingUrl = Global.API_URL + "/moment/get_followings_moment/";
    String getStarUrl = Global.API_URL + "/moment/get_star_moment/";

    String getTagUrl = Global.API_URL + "/moment/get_tag_moment/";


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
            else if (pageAttr == TimelineFragment.STARRED_PAGE){
                getStarUrl += "0" + "/";
                BaseRequest saveRequest = new BaseRequest();
                saveRequest.get(getStarUrl, saveCallback, jwt);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public GetMomentRequest(Callback saveCallback, int pageAttr, String filter, String jwt){
        super();
        try {
            if (pageAttr == TimelineFragment.FOLLOWINGS_PAGE){
                getFollowingUrl += filter + "/0/";
                BaseRequest saveRequest = new BaseRequest();
                saveRequest.get(getFollowingUrl, saveCallback, jwt);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public GetMomentRequest(Callback saveCallback, int pageAttr, String tag, String filter, String jwt){
        super();
        try {
            if (pageAttr == TimelineFragment.TAGGED_PAGE){
                getTagUrl += tag + "/" +  filter + "/0/";
                BaseRequest saveRequest = new BaseRequest();
                saveRequest.get(getTagUrl, saveCallback, jwt);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
